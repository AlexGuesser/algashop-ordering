package it.guesser.algashop.ordering.domain.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Customer {

    private UUID uuid;
    private String fullName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String document;
    private boolean promotionNotificationAllowed;
    private boolean archived;
    private long registeredAt;
    private long archivedAt;
    private int loyaltyPoints;

}
