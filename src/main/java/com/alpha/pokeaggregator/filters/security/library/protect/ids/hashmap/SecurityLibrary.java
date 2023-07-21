package com.alpha.pokeaggregator.filters.security.library.protect.ids.hashmap;

public interface SecurityLibrary {
    String encrypt(String key, String data);
    String decrypt(String key,String data);
}
