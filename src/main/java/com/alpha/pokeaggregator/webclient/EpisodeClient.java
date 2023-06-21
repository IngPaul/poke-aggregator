package com.alpha.pokeaggregator.webclient;

import com.alpha.pokeaggregator.model.Episode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Mono;

public interface EpisodeClient {

    //@Retry(name = "myRetry", fallbackMethod = "fallback")
    //@CircuitBreaker(name = "review-service", fallbackMethod = "fallback")
    public Mono<Episode> getEpisode(Long id);

    //public Mono<Episode> fallback(Exception e);
    //public Mono<Episode> fallback(final Long id, final Throwable t);
    //public Mono<Episode> fallback(Long id, Exception e);
    //public Mono<Episode> fallback(final Long id, final java.net.UnknownHostException t);
    //public Mono<Episode> fallback(final Long id, final java.util.concurrent.TimeoutException t);


}


