package it.guesser.algashop.ordering.domain.exceptions;

public class CustomerAlreadyArchivedException extends DomainException {

    public CustomerAlreadyArchivedException() {
        super(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED);
    }

}
