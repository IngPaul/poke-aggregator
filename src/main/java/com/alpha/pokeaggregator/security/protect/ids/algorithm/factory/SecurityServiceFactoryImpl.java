package com.alpha.pokeaggregator.security.protect.ids.algorithm.factory;

import com.alpha.pokeaggregator.security.protect.ids.algorithm.adapter.SecurityLibraryAdapter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.alpha.pokeaggregator.security.util.Constants.ALGORITHM_HASHIDS;

@RequiredArgsConstructor
@Component
public class SecurityServiceFactoryImpl implements SecurityServiceFactory {

    @Qualifier("defaultLibrary")
    private final SecurityLibraryAdapter defaultLibrary;

    @Qualifier("hashIdsLibrary")
    private final SecurityLibraryAdapter hashIdsLibrary;

    private Map<String, SecurityLibraryAdapter> securityLibraryMap;


    @PostConstruct
    public void init() {
        securityLibraryMap= Map.of(ALGORITHM_HASHIDS, hashIdsLibrary);
    }

    @Override
    public SecurityLibraryAdapter createSecurityService(String encryptionType) {
        return securityLibraryMap.getOrDefault(encryptionType.toUpperCase(), defaultLibrary);
    }
}
