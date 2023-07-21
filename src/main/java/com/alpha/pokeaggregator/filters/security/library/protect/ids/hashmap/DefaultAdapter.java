package com.alpha.pokeaggregator.filters.security.library.protect.ids.hashmap;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DefaultAdapter implements SecurityLibrary {

    @Override
    public String encrypt(String key,String data) {
        return "encrypt by default";
    }

    @Override
    public String decrypt(String key, String data) {
        return "decript by default";
    }
}
