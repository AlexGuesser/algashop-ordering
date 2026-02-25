package it.guesser.algashop.ordering.domain.exceptions;

import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderCannotBePlacedException extends DomainException {

    private OrderCannotBePlacedException(String message) {
        super(message);
    }

    public static OrderCannotBePlacedException noItems(OrderId id) {
        return new OrderCannotBePlacedException(String.format(ErrorMessages.ORDER_CANNOT_BE_PLACED_NO_ITEMS, id));
    }

    public static OrderCannotBePlacedException noRequiredDependency(OrderId id, String dependency) {
        return new OrderCannotBePlacedException(
                String.format(ErrorMessages.ORDER_CANNOT_BE_PLACED_NO_REQUIRED_DEPENDENCY, id, dependency));
    }

}
