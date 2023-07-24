package com.alpha.pokeaggregator.security.protect.ids.algorithm.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import static com.alpha.pokeaggregator.security.util.Constants.DECRYPT_DEFAULT;
import static com.alpha.pokeaggregator.security.util.Constants.ENCRYPT_DEFAULT;

@Component("defaultLibrary")
public class DefaultLibraryAdapterImpl implements SecurityLibraryAdapter {

    @Override
    public String encrypt(String key, JsonNode data) {
        return ENCRYPT_DEFAULT;
    }

    @Override
    public String decrypt(String key, JsonNode data) {
        return DECRYPT_DEFAULT;
    }
}
