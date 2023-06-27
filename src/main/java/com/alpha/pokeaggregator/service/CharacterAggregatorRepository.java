package com.alpha.pokeaggregator.service;

import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.model.CharacterAggregate;
import com.alpha.pokeaggregator.model.Episode;
import java.util.concurrent.CompletionStage;
import reactor.core.publisher.Mono;

public interface CharacterAggregatorRepository {
    CompletionStage<Character> getCharacter( Long episodeId);
}
