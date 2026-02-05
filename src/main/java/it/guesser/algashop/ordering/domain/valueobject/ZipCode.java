package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import org.apache.commons.lang3.StringUtils;

public record ZipCode(String value) {

    public ZipCode {
        value = requireNonNull(trimToNull(value));
        if (StringUtils.length(value) != 5) {
            throw new IllegalArgumentException();
        }
    }

}
