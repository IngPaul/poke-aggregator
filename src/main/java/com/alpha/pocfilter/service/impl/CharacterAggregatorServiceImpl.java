package com.alpha.pocfilter.service.impl;

import com.alpha.pocfilter.mapper.CharacterMapper;
import com.alpha.pocfilter.model.Character;
import com.alpha.pocfilter.model.CharacterAggregate;
import com.alpha.pocfilter.model.Episode;
import com.alpha.pocfilter.service.CharacterAggregatorService;
import com.alpha.pocfilter.utils.Util;
import com.alpha.pocfilter.webclient.CharacterClient;
import com.alpha.pocfilter.webclient.EpisodeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterAggregatorServiceImpl implements CharacterAggregatorService {
    private final CharacterClient characterClient;
    private final EpisodeClient episodeClient;

    @Override
    public Mono<CharacterAggregate> getCharacter(Long characterId, Long episodeId, String status){
        return Mono.zip(
                        this.characterClient.getCharacter(characterId, status),
                        this.episodeClient.getEpisode(episodeId, status)
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
