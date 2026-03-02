package it.guesser.algashop.ordering.domain.factory;

import static java.util.Objects.requireNonNull;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.Shipping;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;

public class OrderFactory {

    private OrderFactory() {
    }

    public static Order filled(CustomerId customerId, Shipping shipping, Billing billing, PaymentMethod paymentMethod,
            Product product, Quantity productQuantity) {

        requireNonNull(customerId);
        requireNonNull(shipping);
        requireNonNull(billing);
        requireNonNull(paymentMethod);
        requireNonNull(product);
        requireNonNull(productQuantity);

        Order order = Order.draft(customerId);

        order.changeShipping(shipping);
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product, productQuantity);

        return order;
    }

}
