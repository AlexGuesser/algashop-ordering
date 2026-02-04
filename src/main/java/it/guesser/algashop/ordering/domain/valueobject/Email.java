package it.guesser.algashop.ordering.domain.valueobject;

import static it.guesser.algashop.ordering.domain.exceptions.ErrorMessages.EMAIL_IS_INVALID;
import static it.guesser.algashop.ordering.domain.validator.FieldsValidation.requireValidEmail;

public record Email(String value) {

    public Email(String value) {
        this.value = requireValidEmail(value, EMAIL_IS_INVALID);
    }

}
