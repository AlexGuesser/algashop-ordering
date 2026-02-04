package it.guesser.algashop.ordering.domain.exceptions;

public class BirthDateInFutureException extends DomainException {

    public BirthDateInFutureException() {
        super(ErrorMessages.BIRTH_DATE_IN_FUTURE);
    }

}
