package org.moonholder.cloud.damocles.security.service.impl;

import org.moonholder.cloud.damocles.security.mapper.UserMapper;
import org.moonholder.cloud.damocles.security.service.IFeignService;
import org.moonholder.cloud.damocles.security.util.JwtUtil;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class FeignServiceImpl implements IFeignService {

    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<String> findAuthsByToken(String token) {
        return jwtUtil.getAuthsFromToken(token);
    }

    @Override
    public List<String> findAuthsByRequest(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        return jwtUtil.getAuthsFromToken(token);
    }

    @Override
    public String findUsernameByRequest(HttpServletRequest request) {
        return jwtUtil.getUsernameFromRequest(request);
    }

    @Override
    public User findUserByRequest(HttpServletRequest request) {
        String username = findUsernameByRequest(request);
        if (StringUtils.hasLength(username)) {
            return userMapper.selectUserByUsername(username);
        }
        return null;
    }
}
