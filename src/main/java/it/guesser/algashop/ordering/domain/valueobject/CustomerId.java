package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import it.guesser.algashop.ordering.domain.utils.IdGenerator;

public record CustomerId(UUID value) {

    public CustomerId() {
        this(IdGenerator.generateTimeBasedUuid());
    }

    public CustomerId(UUID value) {
        this.value = requireNonNull(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
