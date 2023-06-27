package com.alpha.pokeaggregator.controller;

import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.model.CharacterAggregate;
import com.alpha.pokeaggregator.model.Episode;
import com.alpha.pokeaggregator.service.CharacterAggregatorService;
import com.alpha.pokeaggregator.service.CharacterAggregatorServiceGeneral;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("rick-morty")
@RequiredArgsConstructor
@Slf4j
public class CharacterAggregateController {
    private final CharacterAggregatorService characterAggregatorService;
    private final CharacterAggregatorServiceGeneral characterAggregatorServiceGeneral;
    @GetMapping("/{characterId}/{episodeId}")
    public Mono<ResponseEntity<CharacterAggregate>> getProduct(@PathVariable Long characterId, @PathVariable Long episodeId){
        log.info("Request to retrieve data");
        return characterAggregatorService.getCharacter(characterId, episodeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/a/a/a")
    public Mono<ResponseEntity<CharacterAggregate>> getProduct2(){
        log.info("Request to retrieve data 2");
        return Mono.fromCompletionStage(characterAggregatorServiceGeneral.getCharacter1(1L, 1L))
                                         .map(ResponseEntity::ok)
                                         .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/a/a/c")
    public Mono<ResponseEntity<CharacterAggregate>> getProduct3(){
        log.info("Request to retrieve data 3");
        return Mono.fromFuture(characterAggregatorServiceGeneral.getCharacter2(1L, 1L))
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/a/a/c/{episodeId}")
    public Mono<ResponseEntity<Character>> getProduct3( @PathVariable Long episodeId){
        log.info("Request to retrieve data 3");
        return Mono.fromCompletionStage(characterAggregatorServiceGeneral.getCharacter( episodeId))
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
