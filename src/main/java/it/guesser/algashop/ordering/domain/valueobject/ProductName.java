package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;

public record ProductName(String value) {

    public ProductName {
        value = requireNonNull(StringUtils.trimToNull(value));
    }

}
