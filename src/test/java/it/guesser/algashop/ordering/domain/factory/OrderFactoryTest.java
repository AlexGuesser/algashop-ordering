package it.guesser.algashop.ordering.domain.factory;

import static it.guesser.algashop.ordering.domain.entity.OrderTestDataBuilder.aBilling;
import static it.guesser.algashop.ordering.domain.valueobject.ProductDataTestBuilder.aProductInStock;
import static it.guesser.algashop.ordering.domain.valueobject.ShippingTestDataBuilder.aShipping;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.Shipping;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;

public class OrderFactoryTest {

    @Test
    void givenValidInput_whenFilled_thenShouldGenerateOrderReadyToBePlaced() {
        CustomerId customerId = new CustomerId();
        Shipping shipping = aShipping().build();
        Billing billing = aBilling();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
        Product product = aProductInStock();
        Quantity quantity = new Quantity(2);

        Order order = OrderFactory.filled(customerId, shipping, billing, paymentMethod, product, quantity);

        Money expectedItemsTotal = product.price().multiply(quantity);
        Money expectedTotalAmount = expectedItemsTotal.add(shipping.cost());

        assertThat(order).isNotNull();
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getShipping()).isEqualTo(shipping);
        assertThat(order.getBilling()).isEqualTo(billing);
        assertThat(order.getPaymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DRAFT);
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getTotalItems()).isEqualTo(quantity);
        assertThat(order.getTotalAmount()).isEqualTo(expectedTotalAmount);
    }

}
