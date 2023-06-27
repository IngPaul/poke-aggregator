package com.alpha.pokeaggregator.service;

import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.model.CharacterAggregate;
import com.alpha.pokeaggregator.model.Episode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import reactor.core.publisher.Mono;

public interface CharacterAggregatorServiceGeneral {
    CompletionStage<CharacterAggregate> getCharacter1(Long characterId, Long episodeId);
    CompletableFuture<CharacterAggregate> getCharacter2(Long characterId, Long episodeId);

    CompletionStage<Character> getCharacter(Long episodeId);
}
