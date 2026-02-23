package it.guesser.algashop.ordering.domain.exceptions;

import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderCannotBePlacedException extends DomainException {

    public OrderCannotBePlacedException(OrderId orderId) {
        super(String.format(ErrorMessages.ORDER_CANNOT_BE_PLACED, orderId));
    }

}
