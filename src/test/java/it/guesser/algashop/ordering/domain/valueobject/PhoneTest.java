package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    void givenValidValue_whenConstructingPhone_thenValueIsSet() {
        Phone phone = new Phone("555-1234");

        assertThat(phone.value()).isEqualTo("555-1234");
    }

    @Test
    void givenValueWithSpaces_whenConstructingPhone_thenValueIsTrimmed() {
        Phone phone = new Phone("  555-1234  ");

        assertThat(phone.value()).isEqualTo("555-1234");
    }

    @Test
    void givenNullValue_whenConstructingPhone_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Phone(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankValue_whenConstructingPhone_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Phone("   "))
                .isInstanceOf(NullPointerException.class);
    }
}
