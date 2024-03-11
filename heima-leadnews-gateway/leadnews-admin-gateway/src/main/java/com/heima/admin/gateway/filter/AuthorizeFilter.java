package com.heima.admin.gateway.filter;

import com.heima.admin.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取request以及response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //判断是否是登陆,如果是登陆请求则直接放行
        if(request.getURI().getPath().contains("/login")){
            //放行
            return chain.filter(exchange);
        }

       //获取token
        String token = request.getHeaders().getFirst("token");

        //判断token是否存在
        if(StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //检验token是否过期
        try{
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);

            //判断是否过期
            int res = AppJwtUtil.verifyToken(claimsBody);
            if(res == 1 || res == 2){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            //获取用户信息
            Object userId = claimsBody.get("id");


            //存入header
            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.add("userId", userId + "");
            }).build();


            //重置请求
            exchange.mutate().request(serverHttpRequest);
        }catch (Exception e){
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();

        }


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
