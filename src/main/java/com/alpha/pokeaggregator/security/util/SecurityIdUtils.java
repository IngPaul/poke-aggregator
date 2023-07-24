package com.alpha.pokeaggregator.security.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
public class SecurityIdUtils {
    private SecurityIdUtils(){}
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
    public static Mono<Boolean> isLong(JsonNode node) {
        if(node.isNumber()&& node.asLong()>=0)
            return Mono.just(true);
        else
            return isLong(node.asText());
    }
    public static Mono<Boolean> isLong(String str) {
        if (str == null || str.isEmpty())
            return Mono.just(false);
        return Flux.defer(() -> Flux.fromIterable(str.chars()
                        .mapToObj(c -> (char) c)
                        .toList()))
                .all(c -> Character.isDigit(c))
                .map(isAllDigits -> {
                    if (isAllDigits) {
                        try {
                            long number = Long.parseLong(str);
                            return number >= 0;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    return false;
                });
    }

    public static String stringToHex(String input) {
        byte[] bytes = input.getBytes();
        BigInteger bigInt = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "x", bigInt);
    }
    public static String hexToString(String hex) {
        BigInteger bigInt = new BigInteger(hex, 16);
        byte[] bytes = bigInt.toByteArray();
        return new String(bytes);
    }
}
