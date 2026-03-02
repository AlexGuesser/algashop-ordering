package it.guesser.algashop.ordering.domain.exceptions;

import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderCannotBeEditedException extends DomainException {

    public OrderCannotBeEditedException(OrderId orderId, OrderStatus status) {
        super(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, orderId, status));
    }

}
