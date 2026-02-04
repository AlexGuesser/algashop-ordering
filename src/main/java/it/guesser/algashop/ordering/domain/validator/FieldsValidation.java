package it.guesser.algashop.ordering.domain.validator;

import static java.util.Objects.requireNonNull;

import jakarta.annotation.Nullable;

import static org.apache.commons.lang3.StringUtils.trimToNull;

import org.apache.commons.validator.routines.EmailValidator;

public class FieldsValidation {

    private FieldsValidation() {

    }

    public static String requireValidEmail(String email) {
        return requireValidEmail(email, null);

    }

    public static String requireValidEmail(String email, @Nullable String errorMessage) {
        email = requireNonNull(trimToNull(email));

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException(errorMessage);
        }

        return email;
    }

}
