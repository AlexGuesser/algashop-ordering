package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Product;
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

        Product product = new Product(productId, productName, price, true);

        OrderItem orderItem = OrderItem.brandNew(orderId, product, quantity);

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

        Product product = new Product(productId, productName, price, true);

        assertThatThrownBy(() -> OrderItem.brandNew(null, product, quantity))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullQuantity_whenBrandNew_thenThrowsNullPointerException() {
        OrderId orderId = new OrderId();
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Product 1");
        Money price = new Money("10.50");

        Product product = new Product(productId, productName, price, true);

        assertThatThrownBy(() -> OrderItem.brandNew(orderId, product, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenTwoOrderItems_whenCompareEqualsAndHashCode_thenContractIsFollowed() {
        OrderId orderId = new OrderId();
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Product 1");
        Money price = new Money("10.50");
        Quantity quantity = new Quantity(1);

        Product product = new Product(productId, productName, price, true);

        OrderItem orderItem1 = OrderItem.brandNew(orderId, product, quantity);
        OrderItem orderItem2 = OrderItem.brandNew(orderId, product, quantity);

        assertThat(orderItem1).isEqualTo(orderItem1);
        assertThat(orderItem1.hashCode()).isEqualTo(orderItem1.hashCode());

        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void givenOrderItem_whenCompareWithNullAndDifferentTypes_thenEqualsHandlesProperly() {
        OrderId orderId = new OrderId();
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Product 1");
        Money price = new Money("10.50");
        Quantity quantity = new Quantity(1);

        Product product = new Product(productId, productName, price, true);

        OrderItem orderItem = OrderItem.brandNew(orderId, product, quantity);

        assertThat(orderItem).isNotEqualTo(null);
        assertThat(orderItem).isNotEqualTo("some string");
        assertThat(orderItem).isEqualTo(orderItem);
    }
}
