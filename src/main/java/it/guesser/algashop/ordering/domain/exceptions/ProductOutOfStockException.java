package it.guesser.algashop.ordering.domain.exceptions;

import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;

public class ProductOutOfStockException extends DomainException {

    public ProductOutOfStockException(ProductId id) {
        super(String.format(ErrorMessages.PRODUCT_OUT_OF_STOCK, id));
    }

}
