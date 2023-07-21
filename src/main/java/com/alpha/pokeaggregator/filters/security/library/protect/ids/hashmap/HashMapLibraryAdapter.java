package com.alpha.pokeaggregator.filters.security.library.protect.ids.hashmap;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class HashMapLibraryAdapter implements SecurityLibrary {

    @Override
    public String encrypt(String key,String data) {
        return "encrypt by hashmap";
    }

    @Override
    public String decrypt(String key,String data) {
        return "decript by hashmap";
    }
}
