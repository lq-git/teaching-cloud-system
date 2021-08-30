package org.moonholder.cloud.damocles.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.moonholder.cloud.damocles.security.entity.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RefreshScope
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_AUTHORITY = "auth";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            LOGGER.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param user 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, SecurityUser user) {
        String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(SecurityUser user) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        List<String> collect = authorities.stream().filter(p -> StringUtils.hasText(p.getAuthority()))
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUTHORITY, collect);
        return generateToken(claims);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String token = null;
        String header = request.getHeader(tokenHeader);
        if (StringUtils.hasLength(header)) {
            boolean isStartsWith = StringUtils.startsWithIgnoreCase(header, tokenHead);
            if (isStartsWith) {
                token = header.substring(tokenHead.length()).trim();
            }
        }
        return token;
    }

    /**
     * 获取username
     * @param request 请求
     * @return
     */
    public String getUsernameFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return getUsernameFromToken(token);
    }

    public List<String> getAuthsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return getAuthsFromClaims(claims);
    }

    private List<String> getAuthsFromClaims(Claims claims) {
        Object data = claims.get(CLAIM_KEY_AUTHORITY);
        return (ArrayList<String>) data;
    }
}
