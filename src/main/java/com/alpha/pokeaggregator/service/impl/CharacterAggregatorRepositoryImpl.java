package com.alpha.pokeaggregator.service.impl;

import com.alpha.pokeaggregator.mapper.CharacterMapper;
import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.model.CharacterAggregate;
import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.service.CharacterAggregatorRepository;
import com.alpha.pokeaggregator.service.CharacterAggregatorService;
import com.alpha.pokeaggregator.utils.Util;
import com.alpha.pokeaggregator.webclient.CharacterClient;
import com.alpha.pokeaggregator.webclient.EpisodeClient;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


@Service
@RequiredArgsConstructor
public class CharacterAggregatorRepositoryImpl implements CharacterAggregatorRepository {
    private final CharacterClient characterClient;
    private final EpisodeClient episodeClient;

    @Override
    public CompletionStage<Character> getCharacter( Long episodeId){
        return this.characterClient.getCharacter(episodeId).toFuture();
    }

}
