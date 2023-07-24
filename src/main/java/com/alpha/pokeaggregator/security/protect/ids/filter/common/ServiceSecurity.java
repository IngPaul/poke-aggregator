package com.alpha.pokeaggregator.security.protect.ids.filter.common;

import com.alpha.pokeaggregator.security.protect.ids.algorithm.adapter.SecurityLibraryAdapter;
import com.alpha.pokeaggregator.security.protect.ids.algorithm.factory.SecurityServiceFactory;
import com.alpha.pokeaggregator.security.configuration.SecurityIdConfig;
import com.alpha.pokeaggregator.security.protect.ids.filter.dto.ActionEnum;
import com.alpha.pokeaggregator.security.util.Utils;
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

import static com.alpha.pokeaggregator.security.util.Constants.ALGORITHM_HEADER;

@Component
@RequiredArgsConstructor
public class ServiceSecurity {
    private final SecurityIdConfig securityConfig;
    private final SecurityServiceFactory securityServiceFactory;
    private SecurityLibraryAdapter securityLibrary;
    public Flux<DataBuffer> updateBuffer(Publisher<DataBuffer> body, ServerWebExchange exchange, ActionEnum action) {
        List<String> parameter = exchange.getRequest().getHeaders().get(ALGORITHM_HEADER);
        securityLibrary=securityServiceFactory.createSecurityService((parameter==null ||parameter.isEmpty()?"":parameter.get(0)));
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
                    JsonNode node = objectNode.get(fieldName);
                    if (node.isArray()) {
                        ArrayNode arrayNode = (ArrayNode) node;
                         return Flux.fromIterable(arrayNode)
                                .flatMap(element -> updateContentFieldsIdRecursive((ObjectNode) element, actionEnum));
                    } else if (node.isObject()) {
                        return updateContentFieldsIdRecursive((ObjectNode) node, actionEnum);
                    } else if ((node.isTextual() || node.isNumber()) && Boolean.TRUE.equals(hasPattern(fieldName))) {
                        transformId(fieldName, node, actionEnum).map(transformedId->objectNode.put(fieldName, transformedId)).subscribe();
                    }
                    return Flux.empty();
                })
                .then(Mono.just(objectNode));
    }
    private Mono<String> transformId(String key, JsonNode value, ActionEnum action) {
        if(action.equals(ActionEnum.DECRYPT))
            return securityLibrary.decrypt(key, value);
        if(action.equals(ActionEnum.ENCRYPT))
            return securityLibrary.encrypt(key, value);
        return Mono.just(value.asText());
    }

    private Boolean hasPattern(String fieldName) {
        return securityConfig.getPatterns().stream().anyMatch(fieldName::matches);
    }

}
