package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

public class ShippingTestDataBuilder {

    private Money cost = Money.ZERO;
    private LocalDate expectedDate = LocalDate.now().plusWeeks(1);
    private Recipient recipient = aRecipient();
    private Address address = anAddress();

    private ShippingTestDataBuilder() {

    }

    public static ShippingTestDataBuilder aShipping() {
        return new ShippingTestDataBuilder();
    }

    public ShippingTestDataBuilder withCost(Money cost) {
        this.cost = requireNonNull(cost);
        return this;
    }

    public ShippingTestDataBuilder withExpectedDate(LocalDate expectedDate) {
        this.expectedDate = requireNonNull(expectedDate);
        return this;
    }

    public ShippingTestDataBuilder withRecipient(Recipient recipient) {
        this.recipient = requireNonNull(recipient);
        return this;
    }

    public ShippingTestDataBuilder withAddress(Address address) {
        this.address = requireNonNull(address);
        return this;
    }

    public Shipping build() {
        return new Shipping(cost, expectedDate, recipient, address);
    }

    public static Recipient aRecipient() {
        return new Recipient(
                new FullName("John Doe"),
                new Document("12345678901"),
                new Phone("555-1234"));
    }

    public static Address anAddress() {
        return new Address("Main St", "Apt 1", "Downtown", "City", "ST", new ZipCode("12345"));
    }

    public static Shipping shipping(String fullName, String document, String phone,
            String street, String complement, String neighborhood, String city, String state, String zip) {
        return new Shipping(
                Money.ZERO,
                LocalDate.now().plusWeeks(1),
                new Recipient(new FullName(fullName), new Document(document), new Phone(phone)),
                new Address(street, complement, neighborhood, city, state, new ZipCode(zip)));
    }
}
