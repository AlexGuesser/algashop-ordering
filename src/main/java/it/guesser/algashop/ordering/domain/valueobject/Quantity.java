package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        requireNonNull(value);
        if (value < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    public Quantity add(Quantity quantityToAdd) {
        return new Quantity(value + quantityToAdd.value());
    }

    @Override
    public int compareTo(Quantity o) {
        requireNonNull(o);
        return value().compareTo(o.value());
    }

}
