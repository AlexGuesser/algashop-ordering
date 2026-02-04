package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.exceptions.BirthDateInFutureException;
import it.guesser.algashop.ordering.domain.exceptions.ErrorMessages;

public class BirthDateTest {

    @Test
    void givenValidPastDate_whenConstructingBirthDate_thenValueIsSet() {
        LocalDate pastDate = LocalDate.of(1990, 1, 1);

        BirthDate birthDate = new BirthDate(pastDate);

        assertThat(birthDate.value()).isEqualTo(pastDate);
    }

    @Test
    void givenToday_whenConstructingBirthDate_thenValueIsSet() {
        LocalDate today = LocalDate.now();

        BirthDate birthDate = new BirthDate(today);

        assertThat(birthDate.value()).isEqualTo(today);
    }

    @Test
    void givenNullDate_whenConstructingBirthDate_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new BirthDate(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenFutureDate_whenConstructingBirthDate_thenThrowsBirthDateInFutureException() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        assertThatThrownBy(() -> new BirthDate(futureDate))
                .isInstanceOf(BirthDateInFutureException.class)
                .hasMessage(ErrorMessages.BIRTH_DATE_IN_FUTURE);
    }
}
