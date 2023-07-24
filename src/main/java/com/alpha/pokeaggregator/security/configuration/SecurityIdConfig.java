package com.alpha.pokeaggregator.security.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security.ids")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityIdConfig {
    private List<String> patterns;
    private String salt;
}
