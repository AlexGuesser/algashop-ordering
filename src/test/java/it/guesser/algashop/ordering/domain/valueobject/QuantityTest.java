package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class QuantityTest {

    @Test
    void givenValidValue_whenConstructingQuantity_thenValueIsSet() {
        Quantity quantity = new Quantity(10);

        assertThat(quantity.value()).isEqualTo(10);
    }

    @Test
    void givenZeroValue_whenConstructingQuantity_thenValueIsSet() {
        Quantity quantity = new Quantity(0);

        assertThat(quantity.value()).isZero();
    }

    @Test
    void givenNegativeValue_whenConstructingQuantity_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> new Quantity(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity cannot be negative");
    }

    @Test
    void givenNullValue_whenConstructingQuantity_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Quantity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenZeroConstant_whenAccessing_thenValueIsZero() {
        assertThat(Quantity.ZERO.value()).isZero();
    }

    @Test
    void givenTwoQuantities_whenAdding_thenReturnsSum() {
        Quantity first = new Quantity(5);
        Quantity second = new Quantity(3);

        Quantity result = first.add(second);

        assertThat(result.value()).isEqualTo(8);
    }

    @Test
    void givenNullQuantity_whenAdding_thenThrowsNullPointerException() {
        Quantity quantity = new Quantity(5);

        assertThatThrownBy(() -> quantity.add(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenEqualQuantities_whenComparing_thenReturnsZero() {
        Quantity first = new Quantity(10);
        Quantity second = new Quantity(10);

        assertThat(first.compareTo(second)).isZero();
    }

    @Test
    void givenGreaterQuantity_whenComparing_thenReturnsPositive() {
        Quantity first = new Quantity(15);
        Quantity second = new Quantity(10);

        assertThat(first.compareTo(second)).isPositive();
    }

    @Test
    void givenSmallerQuantity_whenComparing_thenReturnsNegative() {
        Quantity first = new Quantity(5);
        Quantity second = new Quantity(10);

        assertThat(first.compareTo(second)).isNegative();
    }

    @Test
    void givenNullQuantity_whenComparing_thenThrowsNullPointerException() {
        Quantity quantity = new Quantity(5);

        assertThatThrownBy(() -> quantity.compareTo(null))
                .isInstanceOf(NullPointerException.class);
    }
}
