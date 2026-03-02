package it.guesser.algashop.ordering.domain.valueobject.id;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import it.guesser.algashop.ordering.domain.utils.IdGenerator;

public record ShoppingCartItemId(UUID vale) {

    public ShoppingCartItemId {
        requireNonNull(vale);
    }

    public ShoppingCartItemId() {
        this(IdGenerator.generateTimeBasedUuid());
    }

}
