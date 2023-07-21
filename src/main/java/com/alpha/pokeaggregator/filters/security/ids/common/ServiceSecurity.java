package com.alpha.pokeaggregator.filters.security.ids.common;

import com.alpha.pokeaggregator.filters.security.ids.dto.ActionEnum;
import com.alpha.pokeaggregator.filters.security.ids.properties.SecurityConfig;
import com.alpha.pokeaggregator.filters.security.ids.util.Utils;
import com.alpha.pokeaggregator.filters.security.library.protect.ids.SecurityServiceFactory;
import com.alpha.pokeaggregator.filters.security.library.protect.ids.hashmap.SecurityLibrary;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.*;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class ServiceSecurity {
    private final SecurityConfig securityConfig;
    private final SecurityServiceFactory securityServiceFactory;
    private SecurityLibrary securityLibrary;
    public Flux<DataBuffer> updateBuffer(Publisher<DataBuffer> body, ServerWebExchange exchange, ActionEnum action) {
        List<String> parameter = exchange.getRequest().getHeaders().get("algoritm");
        securityLibrary=securityServiceFactory.createSecurityService((parameter.isEmpty()?"":parameter.get(0)));
        return Flux.from(body)
                .map(Utils::toJsonString)
                .map(Utils::toJsoNode)
                .flatMap(newData->this.updateContentFieldsIdRecursive((ObjectNode) newData, action))
                .map(newData->Utils.toDataBuffer(newData, exchange.getResponse().bufferFactory()));
    }


    public Mono<ObjectNode> updateContentFieldsIdRecursive(ObjectNode objectNode, ActionEnum actionEnum) {
        return Flux.defer(()-> Flux.fromStream(
                            StreamSupport.stream(
                                    Spliterators.spliteratorUnknownSize(objectNode.fieldNames(), Spliterator.ORDERED),
                                    false)))
                .flatMap(fieldName -> {
                    JsonNode node = objectNode.get(fieldName.toString());
                    if (node.isArray()) {
                        ArrayNode arrayNode = (ArrayNode) node;
                         return Flux.fromIterable(arrayNode)
                                .flatMap(element -> updateContentFieldsIdRecursive((ObjectNode) element, actionEnum));
                    } else if (node.isObject()) {
                        return updateContentFieldsIdRecursive((ObjectNode) node, actionEnum);
                    } else if (node.isTextual() && hasPattern(fieldName.toString())) {
                        String transformedId = transformId(fieldName.toString(), node.asText(), actionEnum);
                        objectNode.put(fieldName.toString(), transformedId);
                    }
                    return Flux.empty();
                })
                .then(Mono.just(objectNode));
    }
    private String transformId(String key, String value, ActionEnum action) {
        if(action.equals(ActionEnum.DECRYPT))
            return securityLibrary.decrypt(key, value);
        if(action.equals(ActionEnum.ENCRYPT))
            return securityLibrary.encrypt(key, value);
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
