package it.guesser.algashop.ordering.domain.entity;

import static it.guesser.algashop.ordering.domain.valueobject.ShippingTestDataBuilder.aShipping;
import static java.util.Objects.requireNonNull;

import it.guesser.algashop.ordering.domain.valueobject.Address;
import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.domain.valueobject.Document;
import it.guesser.algashop.ordering.domain.valueobject.Email;
import it.guesser.algashop.ordering.domain.valueobject.FullName;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Phone;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.Shipping;
import it.guesser.algashop.ordering.domain.valueobject.ZipCode;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Shipping shipping = aShipping().build();
    private Billing billingInfo = aBilling();
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

    public OrderTestDataBuilder withShipping(Shipping shipping) {
        this.shipping = requireNonNull(shipping);
        return this;
    }

    public OrderTestDataBuilder withBillingInfo(Billing billingInfo) {
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
        order.changeShipping(shipping);
        order.changeBilling(billingInfo);
        order.changePaymentMethod(paymentMethod);

        if (withItems) {
            order.addItem(new Product(new ProductId(), new ProductName("Product"), new Money("100.00"), true),
                    new Quantity(2));
            order.addItem(new Product(new ProductId(), new ProductName("Product 2"), new Money("200.00"), true),
                    new Quantity(1));
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

    public static Billing aBilling() {
        return billing(
                "John Doe",
                "12345678901",
                "555-1234",
                anAddress(),
                anEmail());
    }

    private static Address anAddress() {
        return new Address("Main St", "Apt 1", "Downtown", "City", "ST", new ZipCode("12345"));
    }

    public static Email anEmail() {
        return new Email("email@gmail.com");
    }

    public static Billing billing(String fullName, String document, String phone,
            Address address, Email email) {
        return new Billing(
                new FullName(fullName),
                new Document(document),
                new Phone(phone),
                address,
                email);
    }

}
