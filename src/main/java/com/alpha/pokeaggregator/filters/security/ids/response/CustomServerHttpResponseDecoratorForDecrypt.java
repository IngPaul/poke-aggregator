package com.alpha.pokeaggregator.filters.security.ids.response;
import com.alpha.pokeaggregator.filters.security.ids.common.ServiceSecurity;
import com.alpha.pokeaggregator.filters.security.ids.dto.ActionEnum;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.ByteBuffer;


public class CustomServerHttpResponseDecoratorForDecrypt extends ServerHttpResponseDecorator {

    private final ServerWebExchange exchange;
    private final ServiceSecurity serviceSecurity;

    public CustomServerHttpResponseDecoratorForDecrypt(ServerWebExchange exchange, ServiceSecurity serviceSecurity) {
        super(exchange.getResponse());
        this.exchange = exchange;
        this.serviceSecurity = serviceSecurity;
    }
    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> bodyFlux = Flux.from(body);
        Publisher<? extends DataBuffer> newBody=serviceSecurity.updateBuffer(bodyFlux, exchange, ActionEnum.ENCRYPT)
                .map(newBuffer->{
                    ByteBuffer byteBuffer = newBuffer.asByteBuffer();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    exchange.getResponse().getHeaders().setContentLength(bytes.length);
                    return newBuffer;
                });
        return super.writeWith(newBody);
    }
}
