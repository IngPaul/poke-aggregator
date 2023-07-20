package com.alpha.pokeaggregator.filters.security.ids.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.web.server.ServerWebExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isJson(String jsonString) {
        String jsonPattern = "^\\{.*\\}$";
        Pattern pattern = Pattern.compile(jsonPattern);
        Matcher matcher = pattern.matcher(jsonString);
        return matcher.matches();
    }
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
    public static String getValueByPath(JsonNode jsonNode, String path) {
        String[] segments = path.split("\\.");
        JsonNode currentNode = jsonNode;
        for (String segment : segments) {
            currentNode = currentNode.path(segment);
        }
        return currentNode.isMissingNode() ? null : currentNode.asText();
    }
    public static String getParentByPath(JsonNode jsonNode, String path) {
        String[] segments = path.split("\\.");
        JsonNode currentNode = jsonNode;
        for(int i=0; i<segments.length-1;i++)
            currentNode = currentNode.path(segments[i]);
        return currentNode.isMissingNode() ? null : currentNode.asText();
    }
}
