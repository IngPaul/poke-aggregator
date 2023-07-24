package com.alpha.pokeaggregator.security.protect.ids.algorithm.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface SecurityLibraryAdapter {
    Mono<String> encrypt(String key, JsonNode data);
   Mono<String> decrypt(String key, JsonNode data);
}
