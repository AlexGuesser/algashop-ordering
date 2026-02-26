package it.guesser.algashop.ordering.domain.valueobject;

import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;

public class ProductDataTestBuilder {

    private ProductDataTestBuilder() {

    }

    public static Product aProductInStock() {
        return new Product(new ProductId(), new ProductName("Product"), new Money("10.00"), true);
    }

    public static Product aProductOutOfStock() {
        return new Product(new ProductId(), new ProductName("Product"), new Money("10.00"), false);
    }

}
