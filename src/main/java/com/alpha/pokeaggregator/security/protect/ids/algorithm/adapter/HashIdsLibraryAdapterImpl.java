package com.alpha.pokeaggregator.security.protect.ids.algorithm.adapter;

import com.alpha.pokeaggregator.security.protect.ids.algorithm.library.Hashids;
import com.alpha.pokeaggregator.security.configuration.SecurityIdConfig;
import com.alpha.pokeaggregator.security.util.SecurityIdUtils;
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
    public String encrypt(String key, JsonNode data) {
        Hashids hashids = new Hashids(securityIdConfig.getSalt());
        String dataHex = SecurityIdUtils.stringToHex(data.asText());
        String valueEncrypt = hashids.encodeHex(dataHex);
        if(valueEncrypt==null){
            log.warn("There was a fail at the moment to encrypt with Hashids the value {} of property {}", data.asText(), key);
            return ENCRYPT_FAIL;
        }else{
            return valueEncrypt;
        }
    }
    @Override
    public String decrypt(String key, JsonNode data) {
        log.info("Decrypt property {}", key);
        Hashids hashids = new Hashids(securityIdConfig.getSalt());
        String decodeValueHex = hashids.decodeHex(data.asText());
        try {
            return SecurityIdUtils.hexToString(decodeValueHex);
        }catch (NumberFormatException ex){
            log.warn("There was a fail at the moment to decrypt with Hashids the value {} of property {}, error: ", data.asText(), key, ex);
            return DECRYPT_FAIL;
        }
    }
}
