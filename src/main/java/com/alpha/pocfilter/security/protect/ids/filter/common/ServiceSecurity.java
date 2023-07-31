package com.alpha.pocfilter.security.protect.ids.filter.common;

import com.alpha.pocfilter.security.protect.ids.algorithm.adapter.SecurityLibraryAdapter;
import com.alpha.pocfilter.security.protect.ids.algorithm.SecurityServiceFactory;
import com.alpha.pocfilter.security.configuration.SecurityIdConfig;
import com.alpha.pocfilter.security.protect.ids.filter.dto.ActionEnum;
import com.alpha.pocfilter.security.protect.ids.util.SecurityIdUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.alpha.pocfilter.security.util.Constants.ALGORITHM_HEADER;

@Component
@RequiredArgsConstructor
public class ServiceSecurity {
    private final SecurityIdConfig securityConfig;
    private final SecurityServiceFactory securityServiceFactory;
    private final ObjectMapper objectMapper;
    private SecurityLibraryAdapter securityLibrary;
    public Flux<DataBuffer> updateBuffer(Publisher<DataBuffer> body, ServerWebExchange exchange, ActionEnum action) {
        List<String> parameter = exchange.getRequest().getHeaders().get(ALGORITHM_HEADER);
        securityLibrary=securityServiceFactory.createSecurityService((parameter==null ||parameter.isEmpty()?"":parameter.get(0)));
        return Flux.from(body)
                .map(SecurityIdUtils::toJsonString)
                .map(jsonString->SecurityIdUtils.toJsoNode(jsonString, objectMapper))
                .flatMap(newData->this.updateContentFieldsIdRecursive((ObjectNode) newData, action))
                .map(newData-> SecurityIdUtils.toDataBuffer(newData, exchange.getResponse().bufferFactory()));
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
                        objectNode.put(fieldName, transformId(fieldName, node, actionEnum));
                    }
                    return Flux.empty();
                })
                .then(Mono.just(objectNode));
    }
    private String transformId(String key, JsonNode value, ActionEnum action) {
        if(action.equals(ActionEnum.DECRYPT))
            return securityLibrary.decrypt(key, value);
        if(action.equals(ActionEnum.ENCRYPT))
            return securityLibrary.encrypt(key, value);
        return value.asText();
    }

    private Boolean hasPattern(String fieldName) {
        return securityConfig.getPatterns().values().stream().anyMatch(fieldName::matches);
    }

}