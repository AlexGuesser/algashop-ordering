package it.guesser.algashop.ordering.domain.exceptions;

import it.guesser.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import it.guesser.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

public class ShoppingCartItemNotFoundException extends DomainException {

    public ShoppingCartItemNotFoundException(ShoppingCartItemId shoppingCartItemId, ShoppingCartId shoppingCartId) {
        super(String.format(ErrorMessages.SHOPPING_CART_ITEM_NOT_FOUND, shoppingCartItemId, shoppingCartId));
    }

}
