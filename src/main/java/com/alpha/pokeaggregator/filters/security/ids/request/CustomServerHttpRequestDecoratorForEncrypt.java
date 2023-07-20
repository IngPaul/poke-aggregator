package com.alpha.pokeaggregator.filters.security.ids.request;
import com.alpha.pokeaggregator.filters.security.ids.common.ServiceSecurity;
import com.alpha.pokeaggregator.filters.security.ids.dto.ActionEnum;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;


public class CustomServerHttpRequestDecoratorForEncrypt extends ServerHttpRequestDecorator {

    private final ServerWebExchange exchange;
    private final ServiceSecurity serviceSecurity;

    public CustomServerHttpRequestDecoratorForEncrypt( ServerWebExchange exchange, ServiceSecurity serviceSecurity) {
        super(exchange.getRequest());
        this.exchange = exchange;
        this.serviceSecurity = serviceSecurity;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return serviceSecurity.updateBuffer(super.getBody(), exchange, ActionEnum.DECRYPT);
    }
}
