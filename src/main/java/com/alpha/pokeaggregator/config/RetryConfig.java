package com.alpha.pokeaggregator.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
@Slf4j
public class RetryConfig {

    @Value("${retry.maxAttempts}")
    private int maxAttempts;

    @Value("${retry.fixedDelay}")
    private long fixedDelay;

    @Bean
    public Retry retry() {
        return Retry.fixedDelay(maxAttempts, Duration.ofMillis(fixedDelay))
                .doBeforeRetry(this::logRetry); // Agrega el interceptor de registro
    }

    private void logRetry(Retry.RetrySignal retrySignal) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String invokingClass = stackTrace[2].getClassName();
        log.warn("Retrying due to error in class '{}': {}", invokingClass, retrySignal.failure().getMessage());
        log.warn("Retry attempt {}/{}", retrySignal.totalRetries()+1, maxAttempts);
    }

}
