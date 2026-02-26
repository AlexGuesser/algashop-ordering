package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

import it.guesser.algashop.ordering.domain.exceptions.ProductOutOfStockException;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;

public record Product(
        ProductId id,
        ProductName name,
        Money price,
        boolean inStock) {

    public Product {
        requireNonNull(id);
        requireNonNull(name);
        requireNonNull(price);
    }

    public void checkOutOfStock() {
        if (isOutOfStock()) {
            throw new ProductOutOfStockException(id());
        }
    }

    private boolean isOutOfStock() {
        return !inStock;
    }

}
