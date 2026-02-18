package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;

public class OrderItemTest {

    @Test
    void givenValidData_whenBrandNew_thenFieldsAreInitializedCorrectly() {
        OrderId orderId = new OrderId();
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Product 1");
        Money price = new Money("10.50");
        Quantity quantity = new Quantity(3);

        OrderItem orderItem = OrderItem.brandNew(orderId, productId, productName, price, quantity);

        assertThat(orderItem.getId()).isNotNull();
        assertThat(orderItem.getOrderId()).isEqualTo(orderId);
        assertThat(orderItem.getProductId()).isEqualTo(productId);
        assertThat(orderItem.getProductName()).isEqualTo(productName);
        assertThat(orderItem.getProductPrice()).isEqualTo(price);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);

        assertThat(orderItem.getTotalAmount()).isEqualTo(price.multiply(quantity));
    }

    @Test
    void givenNullOrderId_whenBrandNew_thenThrowsNullPointerException() {
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Product 1");
        Money price = new Money("10.50");
        Quantity quantity = new Quantity(1);

        assertThatThrownBy(() -> OrderItem.brandNew(null, productId, productName, price, quantity))
                .isInstanceOf(NullPointerException.class);
    }
}
