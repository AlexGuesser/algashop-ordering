package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

public record Billing(
        FullName fullName,
        Document document,
        Phone phone,
        Address address,
        Email email) {

    public Billing {
        requireNonNull(fullName);
        requireNonNull(document);
        requireNonNull(phone);
        requireNonNull(address);
        requireNonNull(email);
    }

}
