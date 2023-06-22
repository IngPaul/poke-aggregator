package com.alpha.pokeaggregator.service;

import com.alpha.pokeaggregator.model.CharacterAggregate;
import reactor.core.publisher.Mono;

public interface CharacterAggregatorService {
    Mono<CharacterAggregate> getCharacter(Long characterId, Long episodeId, String status);
}
