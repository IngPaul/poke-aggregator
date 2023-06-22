package com.alpha.pokeaggregator.webclient.implement;

import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.webclient.CharacterClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@Slf4j
public class CharacterClientImpl implements CharacterClient {
    private final WebClient client;
    private final WebClient client2;
    private final Retry retry;

    public CharacterClientImpl(WebClient.Builder builder,  Retry retry) {
        this.client = builder.baseUrl("https://rickandmortyapi.com/api/character/").build();
        this.client2 = builder.baseUrl("https://rickandmortyapi.com2/api/character/").build();
        this.retry = retry;
    }
    @Override
    @CircuitBreaker(name = "review-service", fallbackMethod = "fallback")
    public Mono<Character> getCharacter(Long id, String execution){
        if(execution.equals("OK"))
            return this.client
                    .get()
                    .uri("{id}", id)
                    .retrieve()
                    .bodyToMono(Character.class)
                    .retryWhen(retry);
        else
            return this.client2
                    .get()
                    .uri("{id}", id)
                    .retrieve()
                    .bodyToMono(Character.class)
                    .doOnError(e->log.warn("Error in retrieve character [error:{}]", e.getMessage()))
                    .retry(3);
    }
    private Mono<? extends Character> fallback(Exception e) {
        log.info("--------------------------Fallback in character execution 1 [Exception]" );
        log.error("Primary lookup request failed, failing over to Bank Two.  Error was11: " + e.getMessage());
        Character character = new Character();
        character.setId(0L);
        character.setName(e.getMessage());
        return Mono.just(character);
    }


//    public void fallback(CallNotPermittedException ex) {
//        log.info("Fallback to Open Circuit");
//        throw new RuntimeException("Open circuit");
//        // Lógica de fallback exclusiva para el circuito abierto
//    }
}
