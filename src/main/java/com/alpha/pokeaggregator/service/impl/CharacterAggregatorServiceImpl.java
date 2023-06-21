package com.alpha.pokeaggregator.service.impl;

import com.alpha.pokeaggregator.mapper.CharacterMapper;
import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.model.CharacterAggregate;
import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.service.CharacterAggregatorService;
import com.alpha.pokeaggregator.utils.Util;
import com.alpha.pokeaggregator.webclient.CharacterClient;
import com.alpha.pokeaggregator.webclient.EpisodeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterAggregatorServiceImpl implements CharacterAggregatorService {
    private final CharacterClient characterClient;
    private final EpisodeClient episodeClient;

    @Override
//    @Retry(name = "myRetry", fallbackMethod = "fallback")
    public Mono<CharacterAggregate> getCharacter(Long characterId, Long episodeId){
        return Mono.zip(
                        this.characterClient.getCharacter(characterId),
                        this.episodeClient.getEpisode(episodeId)
                )
                .map(data->verifyEpisode(data, characterId));
    }

    private CharacterAggregate verifyEpisode(Tuple2<Character, Episode> characterAndEpisode, Long characterIdFind){
        Character character = characterAndEpisode.getT1();
        CharacterAggregate characterAggregate= CharacterMapper.INSTANCE.parseOfCharacter(character);
        List<String> verifyInEpisodes = characterAndEpisode.getT2().getCharacters().stream().filter(urlCharacter -> Util.getIdOfUrl(urlCharacter) == characterIdFind).collect(Collectors.toList());
        characterAggregate.setIsPresent(verifyInEpisodes.size() > 0);
        characterAggregate.setName(characterAndEpisode.getT1().getName());
        return characterAggregate;
    }
//    public Mono<CharacterAggregate> fallback(Exception e) {
//        return Mono.just(new CharacterAggregate() );
//    }
}
