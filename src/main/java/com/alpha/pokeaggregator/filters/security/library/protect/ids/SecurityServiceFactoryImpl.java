package com.alpha.pokeaggregator.filters.security.library.protect.ids;

import com.alpha.pokeaggregator.filters.security.library.protect.ids.hashmap.DefaultAdapter;
import com.alpha.pokeaggregator.filters.security.library.protect.ids.hashmap.HashMapLibraryAdapter;
import com.alpha.pokeaggregator.filters.security.library.protect.ids.hashmap.SecurityLibrary;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
@RequiredArgsConstructor
@Component
public class SecurityServiceFactoryImpl implements SecurityServiceFactory{
    private Map<String, SecurityLibrary> securityLibraryMap;
    @PostConstruct
    public void init() {
        securityLibraryMap= Map.of("HASHMAP",new HashMapLibraryAdapter());
    }
    @Override
    public SecurityLibrary createSecurityService(String encryptionType) {
        return securityLibraryMap.getOrDefault(encryptionType.toUpperCase(), new DefaultAdapter());
    }
}
