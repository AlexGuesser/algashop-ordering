package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.exceptions.OrderCannotBePlacedException;
import it.guesser.algashop.ordering.domain.exceptions.OrderInvalidShippingDeliveryDateException;
import it.guesser.algashop.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import it.guesser.algashop.ordering.domain.exceptions.ProductOutOfStockException;
import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.Shipping;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;
import static it.guesser.algashop.ordering.domain.entity.OrderTestDataBuilder.*;
import static it.guesser.algashop.ordering.domain.valueobject.ProductDataTestBuilder.aProductInStock;
import static it.guesser.algashop.ordering.domain.valueobject.ProductDataTestBuilder.aProductOutOfStock;
import static it.guesser.algashop.ordering.domain.valueobject.ShippingTestDataBuilder.*;

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
                    assertThat(o.getBilling()).isNull();
                    assertThat(o.getShipping()).isNull();
                    assertThat(o.getPaymentMethod()).isNull();
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
    void givenValidDraftOrder_whenAddingItemWithProductOutOfStock_thenExceptionIsThrown() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).withItems(false).build();

        assertThatThrownBy(
                () -> order.addItem(aProductOutOfStock(), new Quantity(1)))
                .isInstanceOf(ProductOutOfStockException.class);

    }

    @Test
    void givenValidItemData_whenAddItem_thenOrderAndItemAreUpdatedCorrectly() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Product 1");
        Money price = new Money("10.00");
        Quantity quantity = new Quantity(2);

        Product product = new Product(productId, productName, price, true);

        order.addItem(product, quantity);

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

        order.addItem(new Product(new ProductId(), new ProductName("Product 1"), price1, true), quantity1);
        order.addItem(new Product(new ProductId(), new ProductName("Product 2"), price2, true), quantity2);

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
                () -> order.addItem(null, new Quantity(1)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullProductName_whenAddItem_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(() -> order.addItem(
                new Product(new ProductId(), null, new Money("10.00"), true),
                new Quantity(1)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullQuantity_whenAddItem_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(
                () -> order.addItem(
                        new Product(new ProductId(), new ProductName("Product 1"), new Money("10.00"), true),
                        null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).build();

        order.place();

        assertThat(order.isPlaced()).isTrue();

        // IF THE STATUS IS ALREADY PLACED THE ORDER CANNOT BE PLACED AGAIN
        assertThatThrownBy(order::place)
                .isInstanceOf(OrderStatusCannotBeChangedException.class)
                .hasMessageContaining("Cannot change order")
                .hasMessageContaining("from PLACED to PLACED");
    }

    @Test
    void givenDraftOrderWithoutItems_whenPlace_shouldThrowOrderCannotBePlacedException() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).withItems(false).build();

        assertThatThrownBy(order::place)
                .isInstanceOf(OrderCannotBePlacedException.class)
                .hasMessageContaining("cannot be placed because has no items");
    }

    @Test
    void givenOrderWithItemsButWithoutShipping_whenPlace_shouldThrowOrderCannotBePlacedException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        order.addItem(new Product(new ProductId(), new ProductName("Product 1"), new Money("10.00"), true),
                new Quantity(1));

        Billing billingInfo = aBilling();
        order.changeBilling(billingInfo);
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        assertThatThrownBy(order::place)
                .isInstanceOf(OrderCannotBePlacedException.class)
                .hasMessageContaining("cannot be placed because has no required dependency")
                .hasMessageContaining("shipping");
    }

    @Test
    void givenOrderWithItemsButWithoutBilling_whenPlace_shouldThrowOrderCannotBePlacedException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        order.addItem(new Product(new ProductId(), new ProductName("Product 1"), new Money("10.00"), true),
                new Quantity(1));

        Shipping shipping = aShipping().build();

        order.changeShipping(shipping);
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        assertThatThrownBy(order::place)
                .isInstanceOf(OrderCannotBePlacedException.class)
                .hasMessageContaining("cannot be placed because has no required dependency")
                .hasMessageContaining("billingInfo");
    }

    @Test
    void givenOrderWithItemsButWithoutPaymentMethod_whenPlace_shouldThrowOrderCannotBePlacedException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        order.addItem(new Product(new ProductId(), new ProductName("Product 1"), new Money("10.00"), true),
                new Quantity(1));

        Billing billingInfo = aBilling();
        Shipping shipping = aShipping().build();

        order.changeBilling(billingInfo);
        order.changeShipping(shipping);

        assertThatThrownBy(order::place)
                .isInstanceOf(OrderCannotBePlacedException.class)
                .hasMessageContaining("cannot be placed because has no required dependency")
                .hasMessageContaining("paymentMethod");
    }

    @Test
    void givenValidDraftOrder_whenPlace_setsPlacedAtAndKeepsTotals() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).build();

        Money totalAmountBeforePlace = order.getTotalAmount();
        Quantity totalItemsBeforePlace = order.getTotalItems();

        order.place();

        assertThat(order.isPlaced()).isTrue();
        assertThat(order.getPlacedAt()).isNotZero();
        assertThat(order.getTotalAmount()).isEqualTo(totalAmountBeforePlace);
        assertThat(order.getTotalItems()).isEqualTo(totalItemsBeforePlace);
    }

    @Test
    void givenPaidOrder_whenPlace_shouldThrowOrderStatusCannotBeChangedException() {
        Order order = anOrder().withStatus(OrderStatus.PAID).build();

        assertThatThrownBy(order::place)
                .isInstanceOf(OrderStatusCannotBeChangedException.class)
                .hasMessageContaining("Cannot change order")
                .hasMessageContaining("from PAID to PLACED");
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
    void givenValidBilling_whenChangeBilling_thenBillingInfoIsUpdated() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        Billing billing = aBilling();

        order.changeBilling(billing);

        assertThat(order.getBilling()).isEqualTo(billing);

        Billing otherBillingInfo = billing("Jane Doe", "98765432109", "555-9999",
                anAddress(), anEmail());

        order.changeBilling(otherBillingInfo);

        assertThat(order.getBilling()).isEqualTo(otherBillingInfo);
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
    void givenValidShippingData_whenChangeShipping_thenShippingIsUpdated() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        assertThat(order.getShipping()).isNull();

        Shipping shipping = aShipping().build();
        order.changeShipping(shipping);

        assertThat(order.getShipping()).isEqualTo(shipping);
    }

    @Test
    void givenOrderWithItems_whenChangeShipping_thenTotalAmountIncludesShippingCost() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).build();
        Money itemsTotalAmount = order.getTotalAmount();

        Money shippingCost = new Money("10.00");
        Shipping shipping = aShipping().withCost(shippingCost).build();

        order.changeShipping(shipping);

        Money expectedTotalAmount = itemsTotalAmount.add(shippingCost);

        assertThat(order.getShipping()).isEqualTo(shipping);
        assertThat(order.getTotalAmount()).isEqualTo(expectedTotalAmount);
    }

    @Test
    void givenOrderWithItems_whenChangeShippingMultipleTimes_thenTotalAmountIsRecalculatedWithLatestShippingCost() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).build();
        Money itemsTotalAmount = order.getTotalAmount();

        Money firstShippingCost = new Money("10.00");
        Shipping firstShipping = aShipping().withCost(firstShippingCost).build();
        order.changeShipping(firstShipping);

        Money secondShippingCost = new Money("20.00");
        Shipping secondShipping = aShipping().withCost(secondShippingCost).build();
        order.changeShipping(secondShipping);

        Money expectedTotalAmount = itemsTotalAmount.add(secondShippingCost);

        assertThat(order.getShipping()).isEqualTo(secondShipping);
        assertThat(order.getTotalAmount()).isEqualTo(expectedTotalAmount);
    }

    @Test
    void givenExpectedDeliveryDateInPast_whenChangeShipping_thenThrowsOrderInvalidShippingDeliveryDateException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);
        Shipping shipping = aShipping().withExpectedDate(LocalDate.now().minusDays(1)).build();

        assertThatThrownBy(() -> order.changeShipping(shipping))
                .isInstanceOf(OrderInvalidShippingDeliveryDateException.class);
    }

    @Test
    void givenNullShipping_whenChangeShipping_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        assertThatThrownBy(() -> order.changeShipping(null))
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

    @Test
    void givenValidDraftOrder_whenChangeQuantity_thenQuantityIsChangedAndTotalsAreRecalculated() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).withItems(false).build();
        assertThat(order.getItems()).isEmpty();
        assertThat(order.getTotalItems()).isEqualTo(Quantity.ZERO);
        assertThat(order.getTotalAmount()).isEqualTo(Money.ZERO);

        order.addItem(aProductInStock(), new Quantity(1));
        assertThat(order.getItems()).isNotEmpty();
        assertThat(order.getTotalItems()).isEqualTo(new Quantity(1));
        assertThat(order.getTotalAmount()).isEqualTo(new Money("10.00"));
        OrderItem orderItem = order.getItems().stream().findFirst().orElseThrow();
        assertThat(orderItem.getQuantity()).isEqualTo(new Quantity(1));
        assertThat(orderItem.getTotalAmount()).isEqualTo(new Money("10.00"));

        order.changeItemQuantity(orderItem.getId(), new Quantity(2));

        assertThat(order.getItems()).isNotEmpty();
        assertThat(order.getTotalItems()).isEqualTo(new Quantity(2));
        assertThat(order.getTotalAmount()).isEqualTo(new Money("20.00"));
        assertThat(orderItem.getQuantity()).isEqualTo(new Quantity(2));
        assertThat(orderItem.getTotalAmount()).isEqualTo(new Money("20.00"));
    }
}
