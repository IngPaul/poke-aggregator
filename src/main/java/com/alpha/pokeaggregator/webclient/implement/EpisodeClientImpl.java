package com.alpha.pokeaggregator.webclient.implement;

import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.webclient.EpisodeClient;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.Arrays;

@Service
@Slf4j
public class EpisodeClientImpl implements EpisodeClient {
    private final WebClient client;
    private final WebClient client2;
//    private final Retry retry;
    public EpisodeClientImpl(WebClient.Builder builder) {
        this.client = builder.baseUrl("https://rickandmortyapi.com/api/episode/").build();
        this.client2 = builder.baseUrl("https://rickandmortyapi.com2/api/episode/").build();
    }
    @Override
    @CircuitBreaker(name = "review-service", fallbackMethod = "fallback")
    public Mono<Episode> getEpisode(Long id, String execution){
            return this.client
                    .get()
                    .uri("{id}", id)
                    .retrieve()
                    .bodyToMono(Episode.class)
                    .retry(3);
    }
    private  Mono<Episode> fallback(Exception e) {
        log.info("--------------------------Fallback in episode execution 1 [Exception]" );
        log.error("Primary lookup request failed, failing over to Bank Two.  Error was11: " + e.getMessage());
        Episode ep = new Episode();
        ep.setName("circuit-breaker");
        ep.setCharacters(Arrays.asList("circuit-breaker-character"));
        return Mono.just(ep );
    }

    public Mono<Episode> fallback(final Long id, final java.net.UnknownHostException t){
        log.info("--------------------------Fallback execution 2 [UnknownHostException]" );
        log.error("Primary lookup request failed, failing over to Bank Two.  Error was2: " + t.getMessage());
        log.debug("Fallback: routing account lookup request to Bank Two2 {}", id);
        Episode ep = new Episode();
        ep.setName("circuit-breaker-"+id);
        ep.setCharacters(Arrays.asList("circuit-breaker-character"));
        return Mono.just(ep );
    }
    public Mono<Episode> fallback(final Long id, final java.util.concurrent.TimeoutException t){
        log.info("--------------------------Fallback execution 3 [TimeoutException]" );
        log.error("Primary lookup request failed, failing over to Bank Two.  Error was3: " + t.getMessage());
        log.debug("Fallback: routing account lookup request to Bank Two3 {}", id);
        Episode ep = new Episode();
        ep.setName("circuit-breaker-"+id);
        ep.setCharacters(Arrays.asList("circuit-breaker-character"));
        return Mono.just(ep );
    }

}
