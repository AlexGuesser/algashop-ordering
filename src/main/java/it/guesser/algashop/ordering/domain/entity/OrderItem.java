package it.guesser.algashop.ordering.domain.entity;

import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderItemId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;

import static java.util.Objects.requireNonNull;

public class OrderItem {

    private OrderItemId id;
    private OrderId orderId;

    private ProductId productId;
    private ProductName productName;

    private Money productPrice;
    private Quantity quantity;

    private Money totalAmount;

    private OrderItem(OrderItemId id, OrderId orderId, ProductId productId, ProductName productName, Money price,
            Quantity quantity, Money totalAmount) {
        this.id = requireNonNull(id);
        this.orderId = requireNonNull(orderId);
        this.productId = requireNonNull(productId);
        this.productName = requireNonNull(productName);
        this.productPrice = requireNonNull(price);
        this.quantity = requireNonNull(quantity);
        this.totalAmount = requireNonNull(totalAmount);
    }

    protected static OrderItem brandNew(OrderId orderId, ProductId productId, ProductName productName, Money price,
            Quantity quantity) {

        return new OrderItem(
                new OrderItemId(),
                orderId,
                productId,
                productName,
                price,
                quantity,
                price.multiply(quantity));
    }

    public OrderItemId getId() {
        return id;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public ProductName getProductName() {
        return productName;
    }

    public Money getProductPrice() {
        return productPrice;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    private void setId(OrderItemId id) {
        this.id = requireNonNull(id);
    }

    private void setOrderId(OrderId orderId) {
        this.orderId = requireNonNull(orderId);
    }

    private void setProductId(ProductId productId) {
        this.productId = requireNonNull(productId);
    }

    private void setProductName(ProductName productName) {
        this.productName = requireNonNull(productName);
    }

    private void setProductPrice(Money price) {
        this.productPrice = requireNonNull(price);
    }

    private void setQuantity(Quantity quantity) {
        this.quantity = requireNonNull(quantity);
    }

    public void changeQuantity(Quantity quantity) {
        setQuantity(quantity);
        recalculateTotals();
    }

    private void setTotalAmount(Money totalAmount) {
        this.totalAmount = requireNonNull(totalAmount);
    }

    private void recalculateTotals() {
        setTotalAmount(getProductPrice().multiply(getQuantity()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderItem other = (OrderItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
