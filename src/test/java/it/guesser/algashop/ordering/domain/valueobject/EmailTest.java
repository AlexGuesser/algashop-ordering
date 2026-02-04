package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.exceptions.ErrorMessages;

public class EmailTest {

    @Test
    void givenValidEmail_whenConstructingEmail_thenValueIsSet() {
        Email email = new Email("john.doe@example.com");

        assertThat(email.value()).isEqualTo("john.doe@example.com");
    }

    @Test
    void givenNullValue_whenConstructingEmail_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankValue_whenConstructingEmail_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Email("   "))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenInvalidEmail_whenConstructingEmail_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> new Email("invalid-email"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessages.EMAIL_IS_INVALID);
    }
}
