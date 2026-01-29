package it.guesser.algashop.ordering.domain.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class IdGeneratorTest {

    @Test
    void generateTimeBasedUuidShouldReturnNonNullUuid() {
        UUID uuid = IdGenerator.generateTimeBasedUuid();

        assertThat(uuid).isNotNull();
    }

    @Test
    void generateTimeBasedUuidShouldGenerateDifferentValuesOnSubsequentCalls() {
        UUID first = IdGenerator.generateTimeBasedUuid();
        UUID second = IdGenerator.generateTimeBasedUuid();

        assertThat(first).isNotEqualTo(second);
    }

}
