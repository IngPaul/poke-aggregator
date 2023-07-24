package com.alpha.pokeaggregator.controller;

import com.alpha.pokeaggregator.controller.exampleDTO.PersonDTO;
import com.alpha.pokeaggregator.model.CharacterAggregate;
import com.alpha.pokeaggregator.service.CharacterAggregatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("" +
        "")
@RequiredArgsConstructor
@Slf4j
public class CharacterAggregateController {
    private final CharacterAggregatorService characterAggregatorService;
    @GetMapping("/{characterId}/{episodeId}/{status}")
    public Mono<ResponseEntity<CharacterAggregate>> getProduct(@PathVariable Long characterId, @PathVariable Long episodeId, @PathVariable String status){
        log.info("Request to retrieve data");
        return characterAggregatorService.getCharacter(characterId, episodeId, status)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/postSecurityId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PersonDTO>> handleSampleRequest(@RequestBody PersonDTO dto) {
       return  Mono.just(dto)
                .map(ResponseEntity::ok);

    }

}
