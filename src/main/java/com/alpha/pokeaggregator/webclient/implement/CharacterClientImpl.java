package com.alpha.pokeaggregator.webclient.implement;

import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.webclient.CharacterClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CharacterClientImpl implements CharacterClient {
    private final WebClient client;
    public CharacterClientImpl(WebClient.Builder builder) {
        this.client = builder.baseUrl("https://rickandmortyapi.com/api/character/").build();
    }
    @Override
    public Mono<Character> getCharacter(Long id){
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(Character.class);
    }
}
