package com.alpha.pokeaggregator.webclient.implement;

import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.webclient.EpisodeClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

@Service
@Slf4j
public class EpisodeClientImpl implements EpisodeClient {
    private final WebClient client;
    public EpisodeClientImpl(WebClient.Builder builder) {
        this.client = builder.baseUrl("https://rickandmortyapi1.com/api/episode/").build();
    }


    //https://www.baeldung.com/spring-retry
    //https://www.techblogss.com/microservices/microservices-circuit-breaker
    @Override
    @CircuitBreaker(name = "review-service", fallbackMethod = "fallback")
    public Mono<Episode> getEpisode(Long id){
//        throw new RuntimeException("Error");
        log.info("--------------------------Retry 1");
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                //.onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(Episode.class)
                .retry(5)
                .timeout(Duration.ofMillis(300));
                /*.map(r-> {
                    throw new NullPointerException("Error");
                });*/
    }

    /**
     * Fallback method for 4xx exceptions, just percolate the exception back.
     */
    /*private Mono<Episode> fallback(final Long id, final HttpClientErrorException e) {
        log.debug("Account lookup request resulted in a client exception with status {}", e.getStatusCode());
        throw e;
    }*/
    /**
     * Fallback method for all other exception types.
     */
    private  Mono<Episode> fallback(final java.lang.Long id, final java.lang.Throwable t) {
        //log.error("Primary lookup request failed, failing over to Bank Two.  Error was1: " + t.getMessage());
        log.debug("---------------Fallback: routing account lookup request to Bank Two1 {}", id);
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }
    private  Mono<Episode> fallback(final java.lang.Long id, final org.springframework.web.reactive.function.client.WebClientRequestException t) {
        //log.error("Primary lookup request failed, failing over to Bank Two.  Error was1: " + t.getMessage());
        log.debug("---------------Fallback: routing account lookup request to Bank Two1 {}", id);
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }
    private  Mono<Episode> fallback(final java.lang.Long id, final org.springframework.web.reactive.function.client.WebClientResponseException t) {
        //log.error("Primary lookup request failed, failing over to Bank Two.  Error was1: " + t.getMessage());
        log.debug("---------------Fallback: routing account lookup request to Bank Two1 {}", id);
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }

    private  Mono<Episode> fallback(java.lang.Long id, Exception e) {
        log.info("--------------------------Retry 2 " );
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }

    /*
    private  Mono<Episode> fallback(Exception e) {
        log.info("--------------------------Retry 2 " );
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }

    public Mono<Episode> fallback(final Long id, final java.net.UnknownHostException t){
        log.error("Primary lookup request failed, failing over to Bank Two.  Error was2: " + t.getMessage());
        log.debug("Fallback: routing account lookup request to Bank Two2 {}", id);
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }
    public Mono<Episode> fallback(final Long id, final java.util.concurrent.TimeoutException t){
        log.error("Primary lookup request failed, failing over to Bank Two.  Error was3: " + t.getMessage());
        log.debug("Fallback: routing account lookup request to Bank Two3 {}", id);
        Episode ep = new Episode();
        ep.setName("xxxx");
        ep.setCharacters(Arrays.asList("a","b"));
        return Mono.just(ep );
    }

    /**/
}
