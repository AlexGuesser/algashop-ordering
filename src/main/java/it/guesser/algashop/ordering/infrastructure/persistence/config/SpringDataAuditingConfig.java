package it.guesser.algashop.ordering.infrastructure.persistence.config;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider", auditorAwareRef = "auditorProvider")
public class SpringDataAuditingConfig {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> Optional.of(UUID.randomUUID()); // TODO: FIX THAT TO GET THE USER FROM CONTEXT
    }

}
