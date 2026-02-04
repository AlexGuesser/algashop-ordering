package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public record Phone(String value) {

    public Phone(String value) {
        this.value = requireNonNull(trimToNull(value));
    }

}
