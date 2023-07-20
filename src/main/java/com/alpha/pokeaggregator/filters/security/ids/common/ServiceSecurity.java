package com.alpha.pokeaggregator.filters.security.ids.common;

import com.alpha.pokeaggregator.filters.security.ids.dto.ActionEnum;
import com.alpha.pokeaggregator.filters.security.ids.dto.PropertySave;
import com.alpha.pokeaggregator.filters.security.ids.properties.SecurityConfig;
import com.alpha.pokeaggregator.filters.security.ids.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ServiceSecurity {
    private final SecurityConfig securityConfig;
    public Flux<DataBuffer> updateBuffer(Publisher<DataBuffer> body, ServerWebExchange exchange, ActionEnum action) {
        return Flux.from(body)
                .map(Utils::toJsonString)
                .map(Utils::toJsoNode)
                .map(newData->this.updateData(newData, action))
                .map(newData->this.toDataBuffer(newData, exchange));
    }

    private ObjectNode updateData(JsonNode jsonNode, ActionEnum action){
        ObjectNode objectNode = (ObjectNode) jsonNode;
        findFieldsId(jsonNode)
                .stream().parallel()
                .map(field-> {
                    if(action.equals(ActionEnum.DECRYPT))
                        return this.decryptFields(field);
                    if(action.equals(ActionEnum.ENCRYPT))
                        return this.encryptFields(field);
                    return null;
                })
                .map(dataRecover -> {
                    objectNode.put(dataRecover.getKey(), dataRecover.getNewValue());
                    return dataRecover;
                })
                .toList();
        return objectNode;
    }
    private List<PropertySave> findFieldsId(JsonNode jsonNode){
        List<PropertySave> resultList = new ArrayList<>();
        jsonNode.fields().forEachRemaining(entry ->{
            String key = entry.getKey();
            String value = entry.getValue().asText();
            if (hasPattern(key)) {
                PropertySave property= new PropertySave(key, value, null);
                resultList.add(property);
            }
        });
        return resultList;
    }

    private Boolean hasPattern(String fieldName) {
        return securityConfig.getPatterns().stream().anyMatch(pattern -> fieldName.matches(pattern));
    }

    private DataBuffer toDataBuffer(Object objectNode, ServerWebExchange exchange){
        byte[] updatedBytes = objectNode.toString().getBytes();
        return exchange.getResponse().bufferFactory().wrap(updatedBytes);
    }

    private PropertySave encryptFields(PropertySave propertySave){
        /**TODO Conectar con libreria para decodificar en base al value*/
        propertySave.setNewValue("*******");
        return propertySave;
    }
    private PropertySave decryptFields(PropertySave propertySave){
        /**TODO Conectar con libreria para decodificar en base al value*/
        propertySave.setNewValue("DECRYPT");
        return propertySave;
    }

}
