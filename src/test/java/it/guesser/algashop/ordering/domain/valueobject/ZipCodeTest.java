package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ZipCodeTest {

    @Test
    void givenValidZipCode_whenConstructingZipCode_thenValueIsSet() {
        ZipCode zipCode = new ZipCode("12345");

        assertThat(zipCode.value()).isEqualTo("12345");
    }

    @Test
    void givenValueWithSpaces_whenConstructingZipCode_thenValueIsTrimmed() {
        ZipCode zipCode = new ZipCode(" 12345 ");

        assertThat(zipCode.value()).isEqualTo("12345");
    }

    @Test
    void givenNullValue_whenConstructingZipCode_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new ZipCode(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankValue_whenConstructingZipCode_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new ZipCode("   "))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenValueWithInvalidLength_whenConstructingZipCode_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> new ZipCode("1234"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new ZipCode("123456"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

