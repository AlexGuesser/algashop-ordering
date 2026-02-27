package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

public record Recipient(
        FullName fullName,
        Document document,
        Phone phone) {

    public Recipient {
        requireNonNull(fullName);
        requireNonNull(document);
        requireNonNull(phone);
    }

}
