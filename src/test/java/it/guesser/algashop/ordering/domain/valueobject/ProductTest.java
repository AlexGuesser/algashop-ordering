package it.guesser.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.exceptions.ProductOutOfStockException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static it.guesser.algashop.ordering.domain.valueobject.ProductDataTestBuilder.*;

public class ProductTest {

    @Test
    void givenNoProductInStock_whenCheckOutOfStock_thenExceptionIsThrown() {

        assertThatThrownBy(
                () -> aProductOutOfStock().checkOutOfStock()).isInstanceOf(ProductOutOfStockException.class);
    }

    @Test
    void givenProductInStock_whenCheckOutOfStock_NoExceptionIsThrown() {

        assertThatNoException().isThrownBy(
                () -> aProductInStock().checkOutOfStock());

    }

}
