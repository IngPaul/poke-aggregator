package com.alpha.pokeaggregator.security.protect.ids.algorithm;

import com.alpha.pokeaggregator.security.protect.ids.algorithm.adapter.SecurityLibraryAdapter;

public interface SecurityServiceFactory {
    public SecurityLibraryAdapter createSecurityService(String encryptionType);
}