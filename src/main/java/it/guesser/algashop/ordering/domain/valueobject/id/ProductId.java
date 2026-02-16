package it.guesser.algashop.ordering.domain.valueobject.id;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import it.guesser.algashop.ordering.domain.utils.IdGenerator;

public record ProductId(UUID value) {

    public ProductId {
        requireNonNull(value);
    }

    public ProductId() {
        this(IdGenerator.generateTimeBasedUuid());
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
