package com.alpha.pokeaggregator.security.protect.ids.algorithm.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.alpha.pokeaggregator.security.util.Constants.DECRYPT_DEFAULT;
import static com.alpha.pokeaggregator.security.util.Constants.ENCRYPT_DEFAULT;

@Component("defaultLibrary")
public class DefaultLibraryAdapterImpl implements SecurityLibraryAdapter {

    @Override
    public Mono<String> encrypt(String key, JsonNode data) {
        return Mono.just(ENCRYPT_DEFAULT);
    }

    @Override
    public Mono<String> decrypt(String key, JsonNode data) {
        return Mono.just(DECRYPT_DEFAULT);
    }
}
