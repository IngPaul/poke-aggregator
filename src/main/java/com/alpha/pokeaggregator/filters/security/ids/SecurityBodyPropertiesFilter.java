package com.alpha.pokeaggregator.filters.security.ids;

import com.alpha.pokeaggregator.filters.security.ids.common.ServiceSecurity;
import com.alpha.pokeaggregator.filters.security.ids.request.CustomServerHttpRequestDecoratorForEncrypt;
import com.alpha.pokeaggregator.filters.security.ids.response.CustomServerHttpResponseDecoratorForDecrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class SecurityBodyPropertiesFilter implements WebFilter {
    private final ServiceSecurity serviceSecurity;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerWebExchange newExchange = processEncrypt(exchange);
        return chain.filter(newExchange);
    }
    private ServerWebExchange processEncrypt(ServerWebExchange exchange){
        List<String> hasOperationEncrypt = exchange.getRequest().getHeaders().getOrEmpty("encrypt");
        if(!hasOperationEncrypt.isEmpty() && hasOperationEncrypt.get(0).equalsIgnoreCase("ON"))
            return exchange.mutate()
                    .request(buildRequestFilter(exchange))
                    .response(buildResponseFilter(exchange))
                    .build();
        else
            return exchange;
    }
    private ServerHttpResponse buildResponseFilter(ServerWebExchange exchange) {
        return new CustomServerHttpResponseDecoratorForDecrypt(exchange, serviceSecurity) ;
    }
    private ServerHttpRequest buildRequestFilter(ServerWebExchange exchange) {
        return new CustomServerHttpRequestDecoratorForEncrypt(exchange, serviceSecurity);
    }

}
