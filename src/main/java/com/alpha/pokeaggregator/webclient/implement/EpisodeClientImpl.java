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
        this.client = builder.baseUrl("https://rickandmortyapi.com/api/episode/").build();
        this.retry = retry;
    }
    @Override
    @CircuitBreaker( name = "myCircuitBreaker", fallbackMethod = "retrieveEpisodeInOtherRepository")
    public Mono<Episode> getEpisode(Long id){
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(Episode.class)
                .retryWhen(retry);

    }
    private Mono<? extends Episode> retrieveEpisodeInOtherRepository(Long id, Throwable throwable) {
        log.info("*** Execute fallback of circuit breaker, retrieve episode in other repository");
        Episode ep = new Episode();
        ep.setId(id);
        ep.setName("circuit-breaker"+id);
        ep.setCharacters(Arrays.asList("circuit-breaker-character"));
        return Mono.just(ep );
    }
}
