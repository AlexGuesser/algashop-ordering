package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

public record Shipping(
        Money cost,
        LocalDate expectedDate,
        Recipient recipient,
        Address address) {

    public Shipping {
        requireNonNull(cost);
        requireNonNull(expectedDate);
        requireNonNull(recipient);
        requireNonNull(address);
    }

}
