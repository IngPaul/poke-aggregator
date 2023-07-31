package com.alpha.pocfilter.security.protect.ids.algorithm.adapter;

import com.fasterxml.jackson.databind.JsonNode;

public interface SecurityLibraryAdapter {
    String encrypt(String key, JsonNode data);
   String decrypt(String key, JsonNode data);
}
