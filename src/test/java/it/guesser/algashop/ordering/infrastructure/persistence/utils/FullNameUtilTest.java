package it.guesser.algashop.ordering.infrastructure.persistence.utils;

import it.guesser.algashop.ordering.domain.valueobject.FullName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FullNameUtilTest {

    @Nested
    @DisplayName("First Name tests")
    class FirstNameTests {

        @Test
        void givenNullFullName_whenGetFirstName_thenShouldThrowNPE() {
            assertThatThrownBy(
                    () -> FullNameUtil.getFirstName(null)
            ).isExactlyInstanceOf(NullPointerException.class);
        }

        @Test
        void givenSingleName_whenGetFirstName_thenReturnsCorrectFirstName() {
            FullName fullName = new FullName("   Alex    ");

            Optional<String> firstName = FullNameUtil.getFirstName(fullName);

            assertThat(firstName).contains("Alex");
            assertThat(FullNameUtil.getLastName(fullName)).isEmpty();
        }

        @Test
        void givenCompositeName_whenGetFirstName_thenReturnsJustFirstName() {
            FullName fullName = new FullName("   Alex     Pauli   Guesser    ");

            Optional<String> firstName = FullNameUtil.getFirstName(fullName);

            assertThat(firstName).contains("Alex");
            assertThat(FullNameUtil.getLastName(fullName)).contains("Pauli Guesser");
        }

    }


}