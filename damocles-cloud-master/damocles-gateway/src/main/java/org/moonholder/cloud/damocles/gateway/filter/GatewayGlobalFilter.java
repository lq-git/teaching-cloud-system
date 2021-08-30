package org.moonholder.cloud.damocles.gateway.filter;

import cn.hutool.core.util.ReUtil;
import org.moonholder.cloud.damocles.gateway.feign.SecurityFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * @Author moonholder
 * @Description //网关全局过滤器
 * @Date 19:27 2021/1/14
 */
@Component
@RefreshScope
public class GatewayGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private SecurityFeign securityFeign;

    @Value("${spring.cloud.gateway.white-list}")
    private List<String> whiteList;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();
        if (whiteList.stream().noneMatch(regex -> ReUtil.isMatch(regex, path))) {
            String header = request.getHeaders().getFirst(tokenHeader);
            String token = null;
            if (StringUtils.hasLength(header)) {
                boolean isStartsWith = StringUtils.startsWithIgnoreCase(header, tokenHead);
                if (isStartsWith) {
                    token = header.substring(tokenHead.length()).trim();
                }
            }
            // 权限校验
            if (!StringUtils.hasLength(token) || !securityFeign.authentication(token, path)) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }

    @Override
    // order - 过滤器执行优先级，值为整数类型的值范围
    public int getOrder() {
        return 0;
    }
}
