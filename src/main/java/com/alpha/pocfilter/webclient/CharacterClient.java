package com.alpha.pocfilter.webclient;

import com.alpha.pocfilter.model.Character;
import reactor.core.publisher.Mono;

public interface CharacterClient {
    public Mono<Character> getCharacter(Long id, String status);
}
