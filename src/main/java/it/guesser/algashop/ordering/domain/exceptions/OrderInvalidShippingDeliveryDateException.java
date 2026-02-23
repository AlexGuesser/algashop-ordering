package it.guesser.algashop.ordering.domain.exceptions;

import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderInvalidShippingDeliveryDateException extends DomainException {

    public OrderInvalidShippingDeliveryDateException(String message) {
        super(message);
    }

    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ErrorMessages.ORDER_DELIVER_DATE_CANNOT_BE_IN_THE_PAST, id));
    }

}
