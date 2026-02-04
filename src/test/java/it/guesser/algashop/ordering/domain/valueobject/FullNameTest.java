package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class FullNameTest {

    @Test
    void givenValidValue_whenConstructingFullName_thenValueIsSet() {
        FullName fullName = new FullName("John Doe");

        assertThat(fullName.fullName()).isEqualTo("John Doe");
    }

    @Test
    void givenValueWithSpaces_whenConstructingFullName_thenValueIsTrimmed() {
        FullName fullName = new FullName("  John Doe  ");

        assertThat(fullName.fullName()).isEqualTo("John Doe");
    }

    @Test
    void givenNullValue_whenConstructingFullName_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new FullName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankValue_whenConstructingFullName_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new FullName("   "))
                .isInstanceOf(NullPointerException.class);
    }
}
