package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ProductNameTest {

    @Test
    void givenValidValue_whenConstructingProductName_thenValueIsSet() {
        ProductName productName = new ProductName("Premium Widget");

        assertThat(productName.value()).isEqualTo("Premium Widget");
    }

    @Test
    void givenValueWithSpaces_whenConstructingProductName_thenValueIsTrimmed() {
        ProductName productName = new ProductName("  Premium Widget  ");

        assertThat(productName.value()).isEqualTo("Premium Widget");
    }

    @Test
    void givenNullValue_whenConstructingProductName_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new ProductName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankValue_whenConstructingProductName_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new ProductName("   "))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSameValues_whenComparingProductNames_thenAreEqual() {
        ProductName first = new ProductName("Same Product");
        ProductName second = new ProductName("Same Product");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
