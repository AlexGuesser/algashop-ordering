package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.exceptions.OrderCannotBePlacedException;
import it.guesser.algashop.ordering.domain.exceptions.OrderInvalidShippingDeliveryDateException;
import it.guesser.algashop.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import it.guesser.algashop.ordering.domain.valueobject.BillingInfo;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.ShippingInfo;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;
import static it.guesser.algashop.ordering.domain.entity.OrderTestDataBuilder.*;

public class OrderTest {

    @Test
    void givenCustomerId_whenDraft_thenOrderIsInitializedAsDraft() {
        CustomerId customerId = new CustomerId();

        Order order = Order.draft(customerId);

        assertThat(order).satisfies(
                o -> {
                    assertThat(o.getId()).isNotNull();
                    assertThat(o.getCustomerId()).isEqualTo(customerId);
                    assertThat(o.getTotalAmount()).isEqualTo(Money.ZERO);
                    assertThat(o.getTotalItems()).isEqualTo(Quantity.ZERO);
                    assertThat(o.getPlacedAt()).isZero();
                    assertThat(o.getPaidAt()).isZero();
                    assertThat(o.getCanceledAt()).isZero();
                    assertThat(o.getReadyAt()).isZero();
                    assertThat(o.getBillingInfo()).isNull();
                    assertThat(o.getShippingInfo()).isNull();
                    assertThat(o.getPaymentMethod()).isNull();
                    assertThat(o.getShippingCost()).isEqualTo(Money.ZERO);
                    assertThat(o.getExpectedDeliveryDate()).isNull();
                    assertThat(o.getStatus()).isEqualTo(OrderStatus.DRAFT);
                    assertThat(o.getItems()).isNotNull();
                    assertThat(o.getItems()).isEmpty();
                });
    }

    @Test
    void givenNullCustomerId_whenDraft_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> Order.draft(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldGenerateException_whenTryingToAdjustImmutableSetOfOrderItems() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        Set<OrderItem> items = order.getItems();

        assertThatThrownBy(
                () -> items.clear()).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    void givenValidItemData_whenAddItem_thenOrderAndItemAreUpdatedCorrectly() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Product 1");
        Money price = new Money("10.00");
        Quantity quantity = new Quantity(2);

        order.addItem(productId, productName, price, quantity);

        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getTotalItems()).isEqualTo(quantity);
        assertThat(order.getTotalAmount()).isEqualTo(price.multiply(quantity));

        OrderItem item = order.getItems().iterator().next();
        assertThat(item.getOrderId()).isEqualTo(order.getId());
        assertThat(item.getProductId()).isEqualTo(productId);
        assertThat(item.getProductName()).isEqualTo(productName);
        assertThat(item.getProductPrice()).isEqualTo(price);
        assertThat(item.getQuantity()).isEqualTo(quantity);
        assertThat(item.getTotalAmount()).isEqualTo(price.multiply(quantity));
    }

    @Test
    void givenMultipleItems_whenAddItem_thenTotalsAreRecalculatedProperly() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        Money price1 = new Money("5.00");
        Quantity quantity1 = new Quantity(3);
        Money price2 = new Money("7.50");
        Quantity quantity2 = new Quantity(2);

        order.addItem(new ProductId(), new ProductName("Product 1"), price1, quantity1);
        order.addItem(new ProductId(), new ProductName("Product 2"), price2, quantity2);

        Money expectedTotalAmount = price1.multiply(quantity1).add(price2.multiply(quantity2));
        Quantity expectedTotalItems = new Quantity(quantity1.value() + quantity2.value());

        assertThat(order.getItems()).hasSize(2);
        assertThat(order.getTotalItems()).isEqualTo(expectedTotalItems);
        assertThat(order.getTotalAmount()).isEqualTo(expectedTotalAmount);
    }

    @Test
    void givenNullProductId_whenAddItem_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(
                () -> order.addItem(null, new ProductName("Product 1"), new Money("10.00"), new Quantity(1)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullProductName_whenAddItem_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(
                () -> order.addItem(new ProductId(), null, new Money("10.00"), new Quantity(1)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullPrice_whenAddItem_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(
                () -> order.addItem(new ProductId(), new ProductName("Product 1"), null, new Quantity(1)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullQuantity_whenAddItem_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(
                () -> order.addItem(new ProductId(), new ProductName("Product 1"), new Money("10.00"), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        order.addItem(new ProductId(), new ProductName("Product 1"), new Money("10.00"), new Quantity(1));

        BillingInfo billingInfo = aBillingInfo();
        ShippingInfo shippingInfo = aShippingInfo();
        Money shippingCost = new Money("5.00");
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(3);

        order.changeBilling(billingInfo);
        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        order.place();

        assertThat(order.isPlaced()).isTrue();

        // IF THE STATUS IS ALREADY PLACED THE ORDER CANNOT BE PLACED AGAIN
        assertThatThrownBy(
                () -> order.place()).isInstanceOf(OrderStatusCannotBeChangedException.class);
    }

    @Test
    void givenDraftOrderWithoutItems_whenPlace_shouldThrowOrderCannotBePlacedException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        BillingInfo billingInfo = aBillingInfo();
        ShippingInfo shippingInfo = aShippingInfo();
        Money shippingCost = new Money("5.00");
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(3);

        order.changeBilling(billingInfo);
        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        assertThatThrownBy(order::place)
                .isInstanceOf(OrderCannotBePlacedException.class);
    }

    // --- changePaymentMethod ---

    @Test
    void givenValidPaymentMethod_whenChangePaymentMethod_thenPaymentMethodIsUpdated() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        assertThat(order.getPaymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);

        order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE);

        assertThat(order.getPaymentMethod()).isEqualTo(PaymentMethod.GATEWAY_BALANCE);
    }

    @Test
    void givenNullPaymentMethod_whenChangePaymentMethod_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(() -> order.changePaymentMethod(null))
                .isInstanceOf(NullPointerException.class);
    }

    // --- changeBilling ---

    @Test
    void givenValidBillingInfo_whenChangeBilling_thenBillingInfoIsUpdated() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        BillingInfo billingInfo = billingInfo("John Doe", "12345678901", "555-1234",
                "Main St", "Apt 1", "Downtown", "City", "ST", "12345");

        order.changeBilling(billingInfo);

        assertThat(order.getBillingInfo()).isEqualTo(billingInfo);

        BillingInfo otherBillingInfo = billingInfo("Jane Doe", "98765432109", "555-9999",
                "Oak Ave", null, "Suburbs", "Town", "CA", "54321");

        order.changeBilling(otherBillingInfo);

        assertThat(order.getBillingInfo()).isEqualTo(otherBillingInfo);
    }

    @Test
    void givenNullBillingInfo_whenChangeBilling_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(() -> order.changeBilling(null))
                .isInstanceOf(NullPointerException.class);
    }

    // --- changeShipping ---

    @Test
    void givenValidShippingData_whenChangeShipping_thenShippingInfoCostAndDateAreUpdated() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        ShippingInfo shippingInfo = aShippingInfo();
        Money shippingCost = new Money("9.99");
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(5);

        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);

        assertThat(order.getShippingInfo()).isEqualTo(shippingInfo);
        assertThat(order.getShippingCost()).isEqualTo(shippingCost);
        assertThat(order.getExpectedDeliveryDate()).isEqualTo(expectedDeliveryDate);
    }

    @Test
    void givenExpectedDeliveryDateInPast_whenChangeShipping_thenThrowsOrderInvalidShippingDeliveryDateException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        ShippingInfo shippingInfo = aShippingInfo();
        Money shippingCost = new Money("9.99");
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(() -> order.changeShipping(shippingInfo, shippingCost, pastDate))
                .isInstanceOf(OrderInvalidShippingDeliveryDateException.class);
    }

    @Test
    void givenNullShippingInfo_whenChangeShipping_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(() -> order.changeShipping(null, new Money("5.00"), LocalDate.now().plusDays(3)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullShippingCost_whenChangeShipping_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        ShippingInfo shippingInfo = aShippingInfo();

        assertThatThrownBy(() -> order.changeShipping(shippingInfo, null, LocalDate.now().plusDays(3)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullExpectedDeliveryDate_whenChangeShipping_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        ShippingInfo shippingInfo = aShippingInfo();

        assertThatThrownBy(() -> order.changeShipping(shippingInfo, new Money("5.00"), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenValidPlacesOrder_whenMarkAsPaid_thenStatusChangeAndPaidAtIsSet() {
        Order order = anOrder().withStatus(OrderStatus.PLACED).build();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);
        assertThat(order.getPaidAt()).isZero();

        order.markAsPaid();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(order.getPaidAt()).isNotZero();
    }
}
