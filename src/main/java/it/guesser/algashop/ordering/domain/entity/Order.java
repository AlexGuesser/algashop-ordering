package it.guesser.algashop.ordering.domain.entity;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import it.guesser.algashop.ordering.domain.valueobject.BillingInfo;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.ShippingInfo;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;

public class Order {

    private OrderId id;
    private CustomerId customerId;

    private Money totalAmount;
    private Quantity totalItems;

    private long placedAt;
    private long paidAt;
    private long canceledAt;
    private long readyAt;

    private BillingInfo billingInfo;
    private ShippingInfo shippingInfo;

    private OrderStatus status;
    private PaymentMethod paymentMethod;

    private Money shippingCost;
    private LocalDate expectedDeliveryDate;

    private Set<OrderItem> items = new HashSet<>();

    private Order(OrderId id, CustomerId customerId, Money totalAmount, Quantity totalItems, long placedAt, long paidAt,
            long canceledAt, long readyAt, BillingInfo billingInfo, ShippingInfo shippingInfo, OrderStatus status,
            PaymentMethod paymentMethod, Money shippingCost, LocalDate expectedDeliveryDate, Set<OrderItem> items) {
        this.id = requireNonNull(id);
        this.customerId = requireNonNull(customerId);
        this.totalAmount = requireNonNull(totalAmount);
        this.totalItems = requireNonNull(totalItems);
        this.placedAt = placedAt;
        this.paidAt = paidAt;
        this.canceledAt = canceledAt;
        this.readyAt = readyAt;
        this.billingInfo = billingInfo;
        this.shippingInfo = shippingInfo;
        this.status = requireNonNull(status);
        this.paymentMethod = paymentMethod;
        this.shippingCost = shippingCost;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.items = requireNonNull(items);
    }

    public static Order draft(CustomerId customerId) {
        return new Order(
                new OrderId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                0,
                0,
                0,
                0,
                null,
                null,
                OrderStatus.DRAFT,
                null,
                null,
                null,
                new HashSet<>());

    }

    public OrderId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public Quantity getTotalItems() {
        return totalItems;
    }

    public long getPlacedAt() {
        return placedAt;
    }

    public long getPaidAt() {
        return paidAt;
    }

    public long getCanceledAt() {
        return canceledAt;
    }

    public long getReadyAt() {
        return readyAt;
    }

    public BillingInfo getBillingInfo() {
        return billingInfo;
    }

    public ShippingInfo getShippingInfo() {
        return shippingInfo;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Money getShippingCost() {
        return shippingCost;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    private void setTotalAmount(Money totalAmount) {
        this.totalAmount = requireNonNull(totalAmount);
    }

    private void setTotalItems(Quantity totalItems) {
        this.totalItems = requireNonNull(totalItems);
    }

    private void setPlacedAt(long placedAt) {
        this.placedAt = placedAt;
    }

    private void setPaidAt(long paidAt) {
        this.paidAt = paidAt;
    }

    private void setCanceledAt(long canceledAt) {
        this.canceledAt = canceledAt;
    }

    private void setReadyAt(long readyAt) {
        this.readyAt = readyAt;
    }

    private void setBillingInfo(BillingInfo billingInfo) {
        this.billingInfo = requireNonNull(billingInfo);
    }

    private void setShippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = requireNonNull(shippingInfo);
    }

    private void setStatus(OrderStatus status) {
        this.status = requireNonNull(status);
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = requireNonNull(paymentMethod);
    }

    private void setShippingCost(Money shippingCost) {
        this.shippingCost = requireNonNull(shippingCost);
    }

    private void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = requireNonNull(expectedDeliveryDate);
    }

    public void addItem(ProductId productId, ProductName productName, Money price,
            Quantity quantity) {
        OrderItem newOrderItem = OrderItem.brandNew(getId(), productId, productName, price, quantity);

        this.items.add(newOrderItem);
        setTotalAmount(getTotalAmount().add(newOrderItem.getTotalAmount()));
        setTotalItems(getTotalItems().add(quantity));
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
        Order other = (Order) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
