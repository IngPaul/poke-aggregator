package com.alpha.pokeaggregator.service.impl;

import com.alpha.pokeaggregator.mapper.CharacterMapper;
import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.model.CharacterAggregate;
import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.service.CharacterAggregatorRepository;
import com.alpha.pokeaggregator.service.CharacterAggregatorService;
import com.alpha.pokeaggregator.service.CharacterAggregatorServiceGeneral;
import com.alpha.pokeaggregator.utils.Util;
import com.alpha.pokeaggregator.webclient.CharacterClient;
import com.alpha.pokeaggregator.webclient.EpisodeClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


@Service
@RequiredArgsConstructor
public class CharacterAggregatorServiceGeneralImpl implements CharacterAggregatorServiceGeneral {


    private final CharacterAggregatorRepository characterAggregatorRepository;

    @Override
    public CompletionStage<CharacterAggregate> getCharacter1(Long characterId, Long episodeId){
        throw new RuntimeException();
    }

    @Override
    public CompletableFuture<CharacterAggregate> getCharacter2(Long characterId, Long episodeId) {
        return null;
    }

    @Override
    public CompletionStage<Character> getCharacter(Long episodeId) {
        return characterAggregatorRepository.getCharacter(episodeId)
                                            .thenApplyAsync(a -> this.bfobj.apply(a,"aaaaa"))
                                            .thenApplyAsync(this::getCharacter);
    }


    BiFunction<Character, String, Character> bfobj = (x, y) -> {
        System.out.println(x);
        x.setName("hola mundo "+y);
        return x;
    };

    public Character getCharacter(Character episodeId) {

        System.out.println(episodeId);
        episodeId.setGender("hola mamamama");
        return episodeId;
    }


}
