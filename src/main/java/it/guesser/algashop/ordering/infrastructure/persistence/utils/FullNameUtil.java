package it.guesser.algashop.ordering.infrastructure.persistence.utils;

import it.guesser.algashop.ordering.domain.valueobject.FullName;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FullNameUtil {

    private FullNameUtil() {

    }

    public static Optional<String> getFirstName(FullName fullName) {
        Objects.requireNonNull(fullName);

        String[] names = fullName.fullName().split(" ");

        if (names.length < 1) {
            return Optional.empty();
        }

        return Optional.of(names[0].trim());
    }

    public static Optional<String> getLastName(FullName fullName) {
        Objects.requireNonNull(fullName);

        String[] names = fullName.fullName().split(" ");

        if (names.length <= 1) {
            return Optional.empty();
        }

        List<String> namesList = Arrays.stream(Arrays.copyOfRange(names, 1, names.length))
                .filter(Predicate.not(String::isBlank))
                .map(String::trim)
                .toList();
        String lastName = String.join(" ", namesList);
        return Optional.of(lastName);
    }


}
