package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public record Address(
        String street,
        String complement,
        String neighborhood,
        String city,
        String state,
        ZipCode zipCode) {

    private static final String ANONYMOUS = "anonymous";

    public Address {
        street = requireNonNull(trimToNull(street));
        complement = trimToNull(complement);
        neighborhood = requireNonNull(trimToNull(neighborhood));
        city = requireNonNull(trimToNull(city));
        state = requireNonNull(trimToNull(state));
        requireNonNull(zipCode);
    }

    public Address anonymize() {
        return new Address(
                ANONYMOUS,
                ANONYMOUS,
                neighborhood(),
                city(),
                state(),
                zipCode());
    }

}
