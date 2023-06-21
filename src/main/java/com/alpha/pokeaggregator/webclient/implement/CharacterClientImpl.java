package com.alpha.pokeaggregator.webclient.implement;

import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.webclient.CharacterClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.Arrays;

@Service
@Slf4j
public class CharacterClientImpl implements CharacterClient {
    private final WebClient client;
    private final Retry retry;
    public CharacterClientImpl(WebClient.Builder builder, Retry retry) {
//        this.client = builder.baseUrl("https://rickandmortyapi.com/api/character/").build();
        this.client = builder.baseUrl("https://rickandmortyapi.com/api/").build();
        this.retry = retry;
    }
    @Override
    @CircuitBreaker( name = "review-service", fallbackMethod = "retrieveCharacterInOtherRepository")
    public Mono<Character> getCharacter(Long id){
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(Character.class);
//                .retryWhen(retry);
    }

    private Mono<? extends Character> retrieveCharacterInOtherRepository(Long id, Throwable throwable) {
        log.info("*** Execute fallback of circuit breaker, retrieve character in other repository");
        Character character = new Character();
        character.setId(id);
        character.setName("circuit-breaker"+id);
        return Mono.just(character);
    }
}
