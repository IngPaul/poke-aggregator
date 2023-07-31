package com.alpha.pocfilter.security.protect.ids.algorithm.adapter;

import com.alpha.pocfilter.security.protect.ids.algorithm.library.Hashids;
import com.alpha.pocfilter.security.configuration.SecurityIdConfig;
import com.alpha.pocfilter.security.protect.ids.util.SecurityIdUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.alpha.pocfilter.security.util.Constants.*;

@Component("hashIdsLibrary")
@RequiredArgsConstructor
@Slf4j
public class HashIdsLibraryAdapterImpl implements SecurityLibraryAdapter {

    private  final SecurityIdConfig securityIdConfig;
    @Override
    public String encrypt(String key, JsonNode data) {
        log.info("Encrypt response, property {}", key);
        Hashids hashids = new Hashids(securityIdConfig.getSalt());
        try {
            String dataHex = SecurityIdUtils.stringToHex(data.asText());
            String valueEncrypt = hashids.encodeHex(dataHex);
            return valueEncrypt;
        }catch (Exception ex){
            log.error("There was a fail at the moment to encrypt with Hashids the value {} of property {}, error: ", data.asText(), key, ex);
            throw new RuntimeException(INVALID_ID);
        }
    }
    @Override
    public String decrypt(String key, JsonNode data) {
        log.info("Decrypt request, property {}", key);
        Hashids hashids = new Hashids(securityIdConfig.getSalt());
        String decodeValueHex = hashids.decodeHex(data.asText());
        try {
            return SecurityIdUtils.hexToString(decodeValueHex);
        }catch (NumberFormatException ex){
            log.error("There was a fail at the moment to decrypt with Hashids the value {} of property {}, error: ", data.asText(), key, ex);
            throw new RuntimeException(INVALID_ID);
        }
    }
}
