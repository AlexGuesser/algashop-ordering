package it.guesser.algashop.ordering.domain.exceptions;

public class ErrorMessages {

    public static final String EMAIL_IS_INVALID = "E-mail is invalid";
    public static final String REGISTERED_AT_IS_INVALID = "Registered at must be greater than 0";
    public static final String CUSTOMER_ALREADY_ARCHIVED = "Customer is already archive, it is not possible to archive again";
    public static final String BIRTH_DATE_IN_FUTURE = "Birth date cannot be in the future";
    public static final String STATUS_CHANGE_NOT_ALLOWED = "Cannot change order %s status from %s to %s";
    public static final String ORDER_DELIVER_DATE_CANNOT_BE_IN_THE_PAST = "Order:%s cannot have a delivery date in the past";
    public static final String ORDER_CANNOT_BE_PLACED = "Order: %s cannot be placed because has no items";

}
