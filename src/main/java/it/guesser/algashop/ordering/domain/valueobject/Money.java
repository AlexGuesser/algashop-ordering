package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal value) implements Comparable<Money> {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        requireNonNull(value);
        if (ZERO.value().compareTo(value) > 0) {
            throw new IllegalArgumentException("Money cannot be negative");
        }

        value = value.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money(String value) {
        this(new BigDecimal(requireNonNull(value)));
    }

    public Money multiply(Quantity quantity) {
        requireNonNull(quantity);
        return new Money(value().multiply(new BigDecimal(quantity.value())));
    }

    public Money divide(Quantity quantity) {
        requireNonNull(quantity);
        return new Money(value().divide(new BigDecimal(quantity.value())));
    }

    public Money add(Money modeyToAdd) {
        requireNonNull(modeyToAdd);
        return new Money(value().add(modeyToAdd.value()));
    }

    @Override
    public int compareTo(Money o) {
        requireNonNull(o);
        return value().compareTo(o.value);

    }

}
