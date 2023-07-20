package com.alpha.pokeaggregator.controller;

import com.alpha.pokeaggregator.model.CharacterAggregate;
import com.alpha.pokeaggregator.service.CharacterAggregatorService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
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
    public Mono<ResponseEntity<SampleDTO>> handleSampleRequest(@RequestBody SampleDTO dto) {
       return Flux.just(5, 10, 23, 25)
                .map(r1 -> {
                    System.out.println("map del flux principal:/ id:" + Thread.currentThread().getId() + "/ name:" + Thread.currentThread().getName());
                    System.out.println(Thread.currentThread().toString());
                    return r1;
                })
                .flatMap(n -> {
                            System.out.println("flatmap del flux principal: id:" + Thread.currentThread().getId() + "/ name:" + Thread.currentThread().getName());
                            System.out.println(Thread.currentThread().toString());
                            return Flux.just(n * 2)
                                    .map(r -> {
                                        System.out.println("map del flux interno id:" + Thread.currentThread().getId() + "/ name:" + Thread.currentThread().getName());
                                        System.out.println(Thread.currentThread());
                                        return r;
                                    });
                        }
                ).then(Mono.just(dto))
                .map(ResponseEntity::ok);

       // return Mono.just(ResponseEntity.ok(dto));

    }

}
