package it.guesser.algashop.ordering.domain.exceptions;

import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderItemId;

public class OrderItemIdNotFoundInOrderException extends DomainException {

    public OrderItemIdNotFoundInOrderException(OrderItemId orderItemId, OrderId orderId) {
        super(String.format(ErrorMessages.ORDER_ITEM_NOT_FOUND_IN_ORDER, orderItemId, orderId));
    }

}
