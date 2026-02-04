package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public record FullName(String fullName) {

    public FullName(String fullName) {
        this.fullName = requireNonNull(trimToNull(fullName));
    }

    @Override
    public String toString() {
        return fullName;
    }

}
