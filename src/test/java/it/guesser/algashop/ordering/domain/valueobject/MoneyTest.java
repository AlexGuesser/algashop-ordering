package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class MoneyTest {

    @Test
    void givenValidValue_whenConstructingMoney_thenValueIsSet() {
        Money money = new Money(new BigDecimal("99.99"));

        assertThat(money.value()).isEqualByComparingTo(new BigDecimal("99.99"));
    }

    @Test
    void givenValueWithManyDecimals_whenConstructingMoney_thenValueIsRoundedToTwoPlaces() {
        Money money = new Money(new BigDecimal("99.995"));

        assertThat(money.value()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void givenNegativeValue_whenConstructingMoney_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> new Money(new BigDecimal("-10.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Money cannot be negative");
    }

    @Test
    void givenNullValue_whenConstructingMoney_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Money((BigDecimal) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenStringValue_whenConstructingMoney_thenValueIsSet() {
        Money money = new Money("50.50");

        assertThat(money.value()).isEqualByComparingTo(new BigDecimal("50.50"));
    }

    @Test
    void givenNullString_whenConstructingMoney_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Money((String) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenZeroConstant_whenAccessing_thenValueIsZero() {
        assertThat(Money.ZERO.value()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void givenMoneyAndQuantity_whenMultiplying_thenReturnsCorrectAmount() {
        Money money = new Money("10.50");
        Quantity quantity = new Quantity(3);

        Money result = money.multiply(quantity);

        assertThat(result.value()).isEqualByComparingTo(new BigDecimal("31.50"));
    }

    @Test
    void givenNullQuantity_whenMultiplying_thenThrowsNullPointerException() {
        Money money = new Money("10.00");

        assertThatThrownBy(() -> money.multiply(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenMoneyAndQuantity_whenDividing_thenReturnsCorrectAmount() {
        Money money = new Money("30.00");
        Quantity quantity = new Quantity(3);

        Money result = money.divide(quantity);

        assertThat(result.value()).isEqualByComparingTo(new BigDecimal("10.00"));
    }

    @Test
    void givenNullQuantity_whenDividing_thenThrowsNullPointerException() {
        Money money = new Money("30.00");

        assertThatThrownBy(() -> money.divide(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenTwoMoneyValues_whenAdding_thenReturnsSum() {
        Money first = new Money("25.50");
        Money second = new Money("14.25");

        Money result = first.add(second);

        assertThat(result.value()).isEqualByComparingTo(new BigDecimal("39.75"));
    }

    @Test
    void givenNullMoney_whenAdding_thenThrowsNullPointerException() {
        Money money = new Money("10.00");

        assertThatThrownBy(() -> money.add(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenEqualMoneyValues_whenComparing_thenReturnsZero() {
        Money first = new Money("100.00");
        Money second = new Money("100.00");

        assertThat(first.compareTo(second)).isZero();
    }

    @Test
    void givenGreaterMoneyValue_whenComparing_thenReturnsPositive() {
        Money first = new Money("150.00");
        Money second = new Money("100.00");

        assertThat(first.compareTo(second)).isPositive();
    }

    @Test
    void givenSmallerMoneyValue_whenComparing_thenReturnsNegative() {
        Money first = new Money("50.00");
        Money second = new Money("100.00");

        assertThat(first.compareTo(second)).isNegative();
    }

    @Test
    void givenNullMoney_whenComparing_thenThrowsNullPointerException() {
        Money money = new Money("10.00");

        assertThatThrownBy(() -> money.compareTo(null))
                .isInstanceOf(NullPointerException.class);
    }
}
