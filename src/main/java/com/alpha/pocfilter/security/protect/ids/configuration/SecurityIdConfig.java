package com.alpha.pocfilter.security.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ConfigurationProperties(prefix = "security.ids")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityIdConfig {
    private ConcurrentHashMap<String, String> patterns = new ConcurrentHashMap<>();

    private String salt;
}
/*crear una libreria que implemente el envelopment encription*/
// funcionalidad para cargar los roles del token --->
