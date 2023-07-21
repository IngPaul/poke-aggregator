package com.alpha.pokeaggregator.filters.security.ids.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.web.server.ServerWebExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String toJsonString(DataBuffer dataBuffer){
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    public static JsonNode toJsoNode(String stringJson){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(stringJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static DataBuffer toDataBuffer(Object objectNode, DataBufferFactory dataBufferFactory){
        byte[] updatedBytes = objectNode.toString().getBytes();
        return dataBufferFactory.wrap(updatedBytes);
    }
}
