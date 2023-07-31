package com.alpha.pocfilter.service;

import com.alpha.pocfilter.model.CharacterAggregate;
import reactor.core.publisher.Mono;

public interface CharacterAggregatorService {
    Mono<CharacterAggregate> getCharacter(Long characterId, Long episodeId, String status);
}
