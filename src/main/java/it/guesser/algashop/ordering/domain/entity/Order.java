package it.guesser.algashop.ordering.domain.entity;

import static it.guesser.algashop.ordering.domain.validator.FieldsValidation.requireNonNullDependency;
import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import it.guesser.algashop.ordering.domain.exceptions.OrderCannotBePlacedException;
import it.guesser.algashop.ordering.domain.exceptions.OrderInvalidShippingDeliveryDateException;
import it.guesser.algashop.ordering.domain.exceptions.OrderItemIdNotFoundInOrderException;
import it.guesser.algashop.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.Shipping;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderItemId;

public class Order {

    private OrderId id;
    private CustomerId customerId;

    private Money totalAmount = Money.ZERO;
    private Quantity totalItems = Quantity.ZERO;

    private long placedAt;
    private long paidAt;
    private long canceledAt;
    private long readyAt;

    private Billing billing;
    private Shipping shipping;

    private OrderStatus status;
    private PaymentMethod paymentMethod;

    private Set<OrderItem> items = new HashSet<>();

    private Order(OrderId id, CustomerId customerId, Money totalAmount, Quantity totalItems, long placedAt, long paidAt,
            long canceledAt, long readyAt, Billing billingInfo, Shipping shipping, OrderStatus status,
            PaymentMethod paymentMethod, Set<OrderItem> items) {
        this.id = requireNonNull(id);
        this.customerId = requireNonNull(customerId);
        this.totalAmount = requireNonNull(totalAmount);
        this.totalItems = requireNonNull(totalItems);
        this.placedAt = placedAt;
        this.paidAt = paidAt;
        this.canceledAt = canceledAt;
        this.readyAt = readyAt;
        this.billing = billingInfo;
        this.shipping = shipping;
        this.status = requireNonNull(status);
        this.paymentMethod = paymentMethod;
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

    public Billing getBilling() {
        return billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    private Money getShippingCost() {
        return getShipping() == null ? Money.ZERO : getShipping().cost();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Set<OrderItem> getItems() {
        return Set.copyOf(items);
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

    private void setBilling(Billing billingInfo) {
        this.billing = requireNonNull(billingInfo);
    }

    private void setShipping(Shipping shipping) {
        this.shipping = requireNonNull(shipping);
    }

    private void setStatus(OrderStatus status) {
        this.status = requireNonNull(status);
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = requireNonNull(paymentMethod);
    }

    public boolean isDraft() {
        return OrderStatus.DRAFT.equals(getStatus());
    }

    public boolean isPlaced() {
        return OrderStatus.PLACED.equals(getStatus());
    }

    public boolean isPaid() {
        return OrderStatus.PAID.equals(getStatus());
    }

    public void place() {
        validateIfCanBePlaced();

        changeStatus(OrderStatus.PLACED);
        setPlacedAt(Instant.now().toEpochMilli());

    }

    private void validateIfCanBePlaced() {
        requireNonNullDependency(getShipping(),
                OrderCannotBePlacedException.noRequiredDependency(getId(), "shipping"));
        requireNonNullDependency(getBilling(),
                OrderCannotBePlacedException.noRequiredDependency(getId(), "billingInfo"));
        requireNonNullDependency(getPaymentMethod(),
                OrderCannotBePlacedException.noRequiredDependency(getId(), "paymentMethod"));

        if (CollectionUtils.isEmpty(getItems())) {
            throw OrderCannotBePlacedException.noItems(getId());
        }
    }

    public void markAsPaid() {
        changeStatus(OrderStatus.PAID);
        setPaidAt(Instant.now().toEpochMilli());
    }

    private void changeStatus(OrderStatus newStatus) {
        requireNonNull(newStatus);

        if (getStatus().canNotChangeTo(newStatus)) {
            throw new OrderStatusCannotBeChangedException(getId(), getStatus(), newStatus);
        }
        setStatus(newStatus);
    }

    public void addItem(Product product, Quantity quantity) {
        requireNonNull(product);
        requireNonNull(quantity);
        product.checkOutOfStock();

        OrderItem newOrderItem = OrderItem.brandNew(getId(), product, quantity);
        this.items.add(newOrderItem);
        recalculateTotals();
    }

    private void recalculateTotals() {
        BigDecimal newTotalItemsAmmount = getItems().stream().map(
                i -> i.getTotalAmount().value()).reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer newTotalItems = getItems().stream().map(
                i -> i.getQuantity().value()).reduce(0, Integer::sum);

        setTotalAmount(new Money(newTotalItemsAmmount.add(getShippingCost().value())));
        setTotalItems(new Quantity(newTotalItems));
    }

    public void changePaymentMethod(PaymentMethod newPaymentMethod) {
        requireNonNull(newPaymentMethod);
        setPaymentMethod(newPaymentMethod);
    }

    public void changeBilling(Billing billingInfo) {
        requireNonNull(billingInfo);
        setBilling(billingInfo);
    }

    public void changeShipping(Shipping shipping) {
        requireNonNull(shipping);

        if (shipping.expectedDate().isBefore(LocalDate.now())) {
            throw new OrderInvalidShippingDeliveryDateException(getId());
        }

        setShipping(shipping);
        recalculateTotals();
    }

    void changeItemQuantity(OrderItemId orderItemId, Quantity newQuantity) {
        requireNonNull(orderItemId);
        requireNonNull(newQuantity);

        OrderItem orderItem = findOrderItemBy(orderItemId);
        orderItem.changeQuantity(newQuantity);

        recalculateTotals();
    }

    private OrderItem findOrderItemBy(OrderItemId orderItemId) {
        requireNonNull(orderItemId);

        return getItems().stream()
                .filter(oi -> oi.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new OrderItemIdNotFoundInOrderException(orderItemId, getId()));
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
