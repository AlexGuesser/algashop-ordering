package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;

public class OrderTest {

    @Test
    void givenCustomerId_whenDraft_thenOrderIsInitializedAsDraft() {
        CustomerId customerId = new CustomerId();

        Order order = Order.draft(customerId);

        assertThat(order.getId()).isNotNull();
        assertThat(order.getCustomerId()).isEqualTo(customerId);

        assertThat(order.getTotalAmount()).isEqualTo(Money.ZERO);
        assertThat(order.getTotalItems()).isEqualTo(Quantity.ZERO);

        assertThat(order.getPlacedAt()).isZero();
        assertThat(order.getPaidAt()).isZero();
        assertThat(order.getCanceledAt()).isZero();
        assertThat(order.getReadyAt()).isZero();

        assertThat(order.getBillingInfo()).isNull();
        assertThat(order.getShippingInfo()).isNull();
        assertThat(order.getPaymentMethod()).isNull();
        assertThat(order.getShippingCost()).isNull();
        assertThat(order.getExpectedDeliveryDate()).isNull();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.DRAFT);
        assertThat(order.getItems()).isNotNull();
        assertThat(order.getItems()).isEmpty();
    }

    @Test
    void givenNullCustomerId_whenDraft_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> Order.draft(null))
                .isInstanceOf(NullPointerException.class);
    }
}
