package org.moonholder.cloud.damocles.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.moonholder.cloud.damocles.common.core.constant.AuthConstant;
import org.moonholder.cloud.damocles.common.core.constant.CacheConstant;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.common.core.util.TimeFormatter;
import org.moonholder.cloud.damocles.common.core.util.Toolkit;
import org.moonholder.cloud.damocles.common.redis.service.RedisService;
import org.moonholder.cloud.damocles.security.entity.LoginParam;
import org.moonholder.cloud.damocles.security.entity.SecurityAuthority;
import org.moonholder.cloud.damocles.security.entity.SecurityUser;
import org.moonholder.cloud.damocles.security.mapper.UserMapper;
import org.moonholder.cloud.damocles.security.service.IAdminService;
import org.moonholder.cloud.damocles.security.service.IFeignService;
import org.moonholder.cloud.damocles.security.service.IUserService;
import org.moonholder.cloud.damocles.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl implements IAdminService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private IFeignService feignService;
    @Resource
    private IUserService userService;
    @Resource
    private JwtUtil jwtUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RedisService redisService;
    @Resource
    JavaMailSenderImpl mailSender;

    @Override
    public ResponseEntity login(LoginParam loginParam) {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        String verifyCode = loginParam.getVerifyCode();
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password) || !StringUtils.hasLength(verifyCode))
            return ResponseEntity.error("请正确输入登录信息");
        // 冻结验证
        String frozenFlagKey = CacheConstant.FROZEN_FLAG.concat("_").concat(username);
        String frozenCountKey = CacheConstant.FROZEN_COUNT.concat("_").concat(username);
        if (redisService.hasKey(frozenFlagKey)) {
            long frozenTime = redisService.getExpire(frozenFlagKey);
            String timeString = TimeFormatter.format(frozenTime);
            return ResponseEntity.error(String.format("账户已被冻结，请%s之后再次尝试", timeString));
        }
        // 验证码验证
        if (StringUtils.hasLength(verifyCode) && redisService.hasKey(AuthConstant.VERIFY_CODE)) {
            String storageVerifyCode = redisService.getCacheObject(AuthConstant.VERIFY_CODE);
            if (!storageVerifyCode.equalsIgnoreCase(verifyCode)) return ResponseEntity.error("验证码错误，请尝试重新输入");
        }
        // 登录验证 - token颁发
        SecurityUser queryUser = userMapper.selectUserByUsername(username);
        if (Objects.isNull(queryUser)) return ResponseEntity.error("用户名未注册...");
        else if (passwordEncoder.matches(password, queryUser.getPassword())) {
            List<SecurityAuthority> authorities = userMapper.selectAuthByUsername(username);
            queryUser.setAuthorities(authorities);
            String token = jwtUtil.generateToken(queryUser);
            Map<String, String> data = new HashMap<>();
            data.put(AuthConstant.TOKEN_KEY, tokenHead);
            data.put(AuthConstant.TOKEN_VALUE, token);
            redisService.deleteObject(frozenCountKey); // 删除登录失败次数缓存
            return ResponseEntity.success(data);
        } else {
            // 登陆失败次数自增
            if (redisService.hasKey(frozenCountKey)) {
                redisService.increment(frozenCountKey);
            } else {
                redisService.setCacheObject(frozenCountKey, 1, 1L, TimeUnit.HOURS);
            }
            // 登录失败次数大于等于3次 冻结账户24小时
            if ((Integer) redisService.getCacheObject(frozenCountKey) >= 3) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR, 24);
                userService.userFreeze(username, calendar.getTime()); // 冻结账户
                return ResponseEntity.error("由于您在一个小时之内连续登录失败3次，您的账户将被冻结，请24小时之后再次尝试");
            }
        }
        return ResponseEntity.error("用户名或密码有误");
    }

    @Override
    @Transactional
    public boolean register(User user) {
        String password = user.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        user.setPassword(password);
        return userService.save(user) && userService.setRoleTheUser(user, "student");
    }

    @Override
    public ResponseEntity changePassword(HttpServletRequest request, String currentPassword, String password) {
        String username = jwtUtil.getUsernameFromRequest(request);
        User queryUser = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (!passwordEncoder.matches(currentPassword, queryUser.getPassword())) return ResponseEntity.error("当前密码输入有误");
        queryUser.setPassword(passwordEncoder.encode(password));
        return userService.updateById(queryUser) ? ResponseEntity.success("密码修改成功") : ResponseEntity.error("出了一些问题/(ㄒoㄒ)/~~");
    }

    @Override
    @Transactional
    public ResponseEntity changeProfile(User user, HttpServletRequest request) {
        User currentUser = feignService.findUserByRequest(request);
        user.setId(currentUser.getId());
        return userService.updateById(user) ? ResponseEntity.success("资料修改成功") : ResponseEntity.error("出了一些问题/(ㄒoㄒ)/~~");
    }

    @Override
    public ResponseEntity forget(String email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        String forgetToken = Toolkit.getUUIDToken();
        redisService.setCacheObject(forgetToken, email, 5L, TimeUnit.MINUTES);
        try {
            String htmlContent = "您好，尊敬的用户：<br/>您正在进行重置密码<br/><br><b>点击→</b><a style='color:red' href='http://192.168.0.60:80/forget?forgetToken=" + forgetToken + "'>重置密码</a>";
            messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setSubject("damocles - 密码重置");
            messageHelper.setText(htmlContent, true);
            messageHelper.setFrom("moonholder@qq.com");
            messageHelper.setTo(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(Objects.requireNonNull(messageHelper).getMimeMessage());
        return ResponseEntity.success("已发送邮件至您的" + email + "邮箱，请查收邮件进行重置密码操作");
    }

    @Override
    public ResponseEntity forgetPassword(String forgetToken, String password) {
        Object metadata = redisService.getCacheObject(forgetToken);
        if (!(Objects.nonNull(metadata) && StringUtils.hasLength(metadata.toString())))
            return ResponseEntity.error("凭证已过期");
        String username = metadata.toString();
        User user = userMapper.selectUserByUsername(username);
        if (Objects.nonNull(user)) {
            user.setPassword(passwordEncoder.encode(password));
            return userService.updateById(user) ? ResponseEntity.success("快去登录吧。") : ResponseEntity.error("出现了一些问题o(╥﹏╥)o");
        }
        return ResponseEntity.error("绑定的邮箱不存在");
    }
}
