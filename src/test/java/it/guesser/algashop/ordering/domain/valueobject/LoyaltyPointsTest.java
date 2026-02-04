package it.guesser.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LoyaltyPointsTest {

    @Test
    void shouldGenerateWithDefault_whenNoParametersPassedToConstructor() {
        LoyaltyPoints points = new LoyaltyPoints();

        assertThat(points.points()).isZero();
    }

    @Test
    void shouldGenerateWithValuePassed() {
        LoyaltyPoints points = new LoyaltyPoints(10);

        assertThat(points.points()).isEqualTo(10);
    }

    @Test
    void givenNegativePoints_whenConstructingLoyaltyPoints_shouldGenerateException() {
        assertThatThrownBy(
                () -> new LoyaltyPoints(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAddPointsCorrectly() {
        LoyaltyPoints base = new LoyaltyPoints(10);
        LoyaltyPoints toAdd = new LoyaltyPoints(5);

        LoyaltyPoints result = base.add(toAdd);

        assertThat(result.points()).isEqualTo(15);
    }

    @Test
    void givenNullPoints_whenAdding_shouldThrowNullPointerException() {
        LoyaltyPoints base = new LoyaltyPoints(10);

        assertThatThrownBy(() -> base.add(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldSubstractPointsCorrectly() {
        LoyaltyPoints base = new LoyaltyPoints(10);
        LoyaltyPoints toSubstract = new LoyaltyPoints(4);

        LoyaltyPoints result = base.substract(toSubstract);

        assertThat(result.points()).isEqualTo(6);
    }

    @Test
    void givenNullPoints_whenSubstracting_shouldThrowNullPointerException() {
        LoyaltyPoints base = new LoyaltyPoints(10);

        assertThatThrownBy(() -> base.substract(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAllowNegativeResult_whenSubstractingMoreThanExistingPoints() {
        LoyaltyPoints base = new LoyaltyPoints(5);
        LoyaltyPoints toSubstract = new LoyaltyPoints(10);

        assertThatThrownBy(
                () -> base.substract(toSubstract)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void compareToShouldReturnZeroForEqualPoints() {
        LoyaltyPoints first = new LoyaltyPoints(10);
        LoyaltyPoints second = new LoyaltyPoints(10);

        assertThat(first.compareTo(second)).isZero();
    }

    @Test
    void compareToShouldReturnPositiveWhenFirstIsGreater() {
        LoyaltyPoints first = new LoyaltyPoints(15);
        LoyaltyPoints second = new LoyaltyPoints(10);

        assertThat(first.compareTo(second)).isPositive();
    }

    @Test
    void compareToShouldReturnNegativeWhenFirstIsSmaller() {
        LoyaltyPoints first = new LoyaltyPoints(5);
        LoyaltyPoints second = new LoyaltyPoints(10);

        assertThat(first.compareTo(second)).isNegative();
    }

}
