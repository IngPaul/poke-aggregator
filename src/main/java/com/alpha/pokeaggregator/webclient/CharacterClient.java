package com.alpha.pokeaggregator.webclient;

import com.alpha.pokeaggregator.model.Character;
import reactor.core.publisher.Mono;

public interface CharacterClient {
    public Mono<Character> getCharacter(Long id, String status);
}
