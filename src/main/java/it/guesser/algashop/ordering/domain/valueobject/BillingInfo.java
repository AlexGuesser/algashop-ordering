package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

public record BillingInfo(
        FullName fullName,
        Document document,
        Phone phone,
        Address address) {

    public BillingInfo {
        requireNonNull(fullName);
        requireNonNull(document);
        requireNonNull(phone);
        requireNonNull(address);
    }

}
