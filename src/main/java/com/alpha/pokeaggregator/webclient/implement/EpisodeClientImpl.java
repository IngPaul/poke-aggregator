package com.alpha.pokeaggregator.webclient.implement;

import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.webclient.EpisodeClient;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;


import java.util.Arrays;

@Service
@Slf4j

public class EpisodeClientImpl implements EpisodeClient {
    private final WebClient client;
    private final Retry retry;
    public EpisodeClientImpl(WebClient.Builder builder, Retry retry) {
        this.client = builder.baseUrl("https://rickandmortyapi.com/api/").build();
        this.retry = retry;
    }
    @Override
//    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @CircuitBreaker( name = "myCircuitBreaker", fallbackMethod = "fallback2")
    public Mono<Episode> getEpisode(Long id){
//        throw new RuntimeException("Error");
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(Episode.class)
                .retryWhen(retry);

    }

    private Mono<? extends Episode> fallback2(Throwable throwable) {
        return Mono.error(new RuntimeException("Circuit Error"));
    }

    private Mono<? extends Episode> fallback(Throwable throwable) {
        log.info("Retry");
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }
}
