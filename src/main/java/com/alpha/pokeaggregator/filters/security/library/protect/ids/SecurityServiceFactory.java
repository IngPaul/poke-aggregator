package com.alpha.pokeaggregator.filters.security.library.protect.ids;

import com.alpha.pokeaggregator.filters.security.library.protect.ids.hashmap.SecurityLibrary;

import java.util.Map;

public interface SecurityServiceFactory {
    public SecurityLibrary createSecurityService(String encryptionType);
}
