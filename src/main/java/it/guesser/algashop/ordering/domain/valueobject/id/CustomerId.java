package it.guesser.algashop.ordering.domain.valueobject.id;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import it.guesser.algashop.ordering.domain.utils.IdGenerator;

public record CustomerId(UUID value) {

    public CustomerId {
        requireNonNull(value);
    }

    public CustomerId() {
        this(IdGenerator.generateTimeBasedUuid());
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
