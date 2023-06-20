package com.alpha.pokeaggregator.webclient.implement;

import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.webclient.EpisodeClient;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.RetryBackoffSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.RetryBackoffSpec;


import java.time.Duration;
import java.util.Arrays;

@Service
@Slf4j
public class EpisodeClientImpl implements EpisodeClient {
    private final WebClient client;
    public EpisodeClientImpl(WebClient.Builder builder) {
        this.client = builder.baseUrl("https://rickandmortyapi.com/api/episode/").build();
    }
    @Override
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    public Mono<Episode> getEpisode(Long id){
//        throw new RuntimeException("Error");
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(Episode.class)
                .map(r-> {
                    throw new RuntimeException("Error");
                });
    }

    public Mono<Episode> fallback(Exception e) {
        log.info("Retry");
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }
}
