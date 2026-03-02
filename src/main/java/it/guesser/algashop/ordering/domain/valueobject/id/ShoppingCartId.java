package it.guesser.algashop.ordering.domain.valueobject.id;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import it.guesser.algashop.ordering.domain.utils.IdGenerator;

public record ShoppingCartId(UUID value) {

    public ShoppingCartId {
        requireNonNull(value);
    }

    public ShoppingCartId() {
        this(IdGenerator.generateTimeBasedUuid());
    }

}
