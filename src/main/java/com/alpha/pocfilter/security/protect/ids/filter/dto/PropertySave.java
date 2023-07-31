package com.alpha.pocfilter.security.protect.ids.filter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertySave {
    private String key;
    private String oldValue;
    private String newValue;
}
