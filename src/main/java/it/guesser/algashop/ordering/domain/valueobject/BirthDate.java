package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import it.guesser.algashop.ordering.domain.exceptions.BirthDateInFutureException;

public record BirthDate(LocalDate value) {

    public BirthDate(LocalDate value) {
        requireNonNull(value);

        if (value.isAfter(LocalDate.now())) {
            throw new BirthDateInFutureException();
        }

        this.value = value;
    }

}
