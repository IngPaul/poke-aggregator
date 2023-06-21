package com.alpha.pokeaggregator.config;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfig {
    @Bean
    public CircuitBreakerConfigCustomizer reviewService(){
        return CircuitBreakerConfigCustomizer.of("myCircuitBreaker", builder ->
                builder.minimumNumberOfCalls(4)
                        .waitDurationInOpenState(Duration.ofSeconds(300))
                        .permittedNumberOfCallsInHalfOpenState(2)
        );
    }
}

