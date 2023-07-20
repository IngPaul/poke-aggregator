package com.alpha.pokeaggregator.filters.security.ids.common;

import com.alpha.pokeaggregator.filters.security.ids.dto.ActionEnum;
import com.alpha.pokeaggregator.filters.security.ids.dto.PropertySave;
import com.alpha.pokeaggregator.filters.security.ids.properties.SecurityConfig;
import com.alpha.pokeaggregator.filters.security.ids.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
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
                .map(newData->this.updateContentFieldsId((ObjectNode) newData, action))
                .map(newData->Utils.toDataBuffer(newData, exchange.getResponse().bufferFactory()));
    }
    private ObjectNode updateContentFieldsId(ObjectNode objectNode, ActionEnum actionEnum){
        Iterator<String> fieldNames = objectNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode node = objectNode.get(fieldName);
            if (node.isArray()) {
                ArrayNode arrayNode = (ArrayNode) node;
                for (JsonNode element : arrayNode) {
                    updateContentFieldsId((ObjectNode) element, actionEnum);
                }
            } else if (node.isObject()) {
                updateContentFieldsId((ObjectNode) node, actionEnum);
            }else if (node.isTextual()) {
                if (hasPattern(fieldName)) {
                    String transformId = transformId(fieldName, node.asText(), actionEnum);
                    objectNode.put(fieldName, transformId);
                }
            }
        }
        return objectNode;
    }

    private String transformId(String key, String value, ActionEnum action) {
        if(action.equals(ActionEnum.DECRYPT))
            return this.decryptFields(key, value);
        if(action.equals(ActionEnum.ENCRYPT))
            return this.encryptFields(key, value);
        return value;
    }

    private Boolean hasPattern(String fieldName) {
        return securityConfig.getPatterns().stream().anyMatch(pattern -> fieldName.matches(pattern));
    }

    private String encryptFields(String key, String value){
        /**TODO Conectar con libreria para decodificar en base al value*/
        return "*******";
    }
    private String decryptFields(String key, String value){
        /**TODO Conectar con libreria para decodificar en base al value*/
        return "DECRYPT";
    }

}
