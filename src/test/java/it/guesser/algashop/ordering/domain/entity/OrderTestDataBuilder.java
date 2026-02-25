package it.guesser.algashop.ordering.domain.entity;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import it.guesser.algashop.ordering.domain.valueobject.Address;
import it.guesser.algashop.ordering.domain.valueobject.BillingInfo;
import it.guesser.algashop.ordering.domain.valueobject.Document;
import it.guesser.algashop.ordering.domain.valueobject.FullName;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Phone;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.ShippingInfo;
import it.guesser.algashop.ordering.domain.valueobject.ZipCode;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Money shippingCost = Money.ZERO;
    private LocalDate expectedDeliveryDate = LocalDate.now().plusWeeks(1);
    private ShippingInfo shippingInfo = aShippingInfo();
    private BillingInfo billingInfo = aBillingInfo();
    private boolean withItems = true;
    private OrderStatus status;

    private OrderTestDataBuilder() {

    }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public OrderTestDataBuilder withCustomerId(CustomerId customerId) {
        this.customerId = requireNonNull(customerId);
        return this;
    }

    public OrderTestDataBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = requireNonNull(paymentMethod);
        return this;
    }

    public OrderTestDataBuilder withShippingCost(Money shippingCost) {
        this.shippingCost = requireNonNull(shippingCost);
        return this;
    }

    public OrderTestDataBuilder withExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = requireNonNull(expectedDeliveryDate);
        return this;
    }

    public OrderTestDataBuilder withShippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = requireNonNull(shippingInfo);
        return this;
    }

    public OrderTestDataBuilder withBillingInfo(BillingInfo billingInfo) {
        this.billingInfo = requireNonNull(billingInfo);
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder withStatus(OrderStatus status) {
        this.status = requireNonNull(status);
        return this;
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);
        order.changeBilling(billingInfo);
        order.changePaymentMethod(paymentMethod);

        if (withItems) {
            order.addItem(new ProductId(), new ProductName("Product"), new Money("100.00"), new Quantity(2));
            order.addItem(new ProductId(), new ProductName("Product 2"), new Money("200.00"), new Quantity(1));
        }

        switch (status) {
            case DRAFT -> {
            }

            case PLACED -> {
                order.place();
            }

            case PAID -> {
                order.place();
                order.markAsPaid();
            }

            case READY -> {
            }

            case CANCELED -> {
            }

            default -> {
            }
        }

        return order;
    }

    public static BillingInfo aBillingInfo() {
        return billingInfo(
                "John Doe",
                "12345678901",
                "555-1234",
                "Main St", "Apt 1", "Downtown", "City", "ST", "12345");
    }

    public static ShippingInfo aShippingInfo() {
        return shippingInfo(
                "John Doe",
                "12345678901",
                "555-1234",
                "Main St", "Apt 1", "Downtown", "City", "ST", "12345");
    }

    public static Address anAddress() {
        return null;
    }

    public static BillingInfo billingInfo(String fullName, String document, String phone,
            String street, String complement, String neighborhood, String city, String state, String zip) {
        return new BillingInfo(
                new FullName(fullName),
                new Document(document),
                new Phone(phone),
                new Address(street, complement, neighborhood, city, state, new ZipCode(zip)));
    }

    public static ShippingInfo shippingInfo(String fullName, String document, String phone,
            String street, String complement, String neighborhood, String city, String state, String zip) {
        return new ShippingInfo(
                new FullName(fullName),
                new Document(document),
                new Phone(phone),
                new Address(street, complement, neighborhood, city, state, new ZipCode(zip)));
    }
}
