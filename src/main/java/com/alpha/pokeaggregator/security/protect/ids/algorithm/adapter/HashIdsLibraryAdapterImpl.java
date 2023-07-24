package com.alpha.pokeaggregator.security.protect.ids.algorithm.adapter;

import com.alpha.pokeaggregator.security.protect.ids.algorithm.library.Hashids;
import com.alpha.pokeaggregator.security.configuration.SecurityIdConfig;
import com.alpha.pokeaggregator.security.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.alpha.pokeaggregator.security.util.Constants.DECRYPT_FAIL;
import static com.alpha.pokeaggregator.security.util.Constants.ENCRYPT_FAIL;

@Component("hashIdsLibrary")
@RequiredArgsConstructor
@Slf4j
public class HashIdsLibraryAdapterImpl implements SecurityLibraryAdapter {
    private  final SecurityIdConfig securityIdConfig;
    @Override
    public Mono<String> encrypt(String key, JsonNode data) {
        Hashids a = new Hashids(securityIdConfig.getSalt());
        return Utils.isLong(data).map(verify->{
                    if(verify.equals(Boolean.TRUE))
                        return a.encode(data.asLong());
                    else {
                        log.warn("There was a fail at the moment to encrypt with Hashids the value {} of property {}", data.asText(), key);
                        return ENCRYPT_FAIL;
                    }
                });
    }
    @Override
    public Mono<String> decrypt(String key, JsonNode data) {
        Hashids a = new Hashids(securityIdConfig.getSalt());
        long[] decodeValue = a.decode(data.asText());
        if(decodeValue.length > 0)
            return Mono.just(String.valueOf(decodeValue[0]));
        else {
            log.warn("There was a fail at the moment to decrypt with Hashids the value {} of property {}", data.asText(), key);
            return Mono.just(DECRYPT_FAIL);
        }
    }
}
