package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

public record ShippingInfo(
        FullName fullName,
        Document document,
        Phone phone,
        Address address) {

    public ShippingInfo {
        requireNonNull(fullName);
        requireNonNull(document);
        requireNonNull(phone);
        requireNonNull(address);
    }

}
