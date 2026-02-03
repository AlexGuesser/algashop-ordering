package it.guesser.algashop.ordering.domain.exceptions;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
