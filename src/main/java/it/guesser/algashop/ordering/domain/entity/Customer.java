package it.guesser.algashop.ordering.domain.entity;

import static it.guesser.algashop.ordering.domain.validator.FieldsValidation.requireValidEmail;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import it.guesser.algashop.ordering.domain.utils.IdGenerator;

import static it.guesser.algashop.ordering.domain.exceptions.ErrorMessages.*;

public class Customer {

    private static final String ANONYMOUS_FULL_NAME = "Anonymous";
    private static final String ANONYMOUS_PHONE = "000-000-0000";
    private static final String ANONYMOUS_DOCUMENT = "000-00-0000";
    private static final String ANONYMOUS_EMAIL_SUFFIX = "@anonymous.com";

    private UUID uuid;
    private String fullName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String document;
    private long registeredAt;
    private boolean promotionNotificationsAllowed;
    private boolean archived;
    private long archivedAt;
    private int loyaltyPoints;

    public Customer(String fullName, LocalDate birthDate, String email, String phone, String document) {
        setUuid(IdGenerator.generateTimeBasedUuid());
        setRegisteredAt(Instant.now().toEpochMilli());
        setFullName(fullName);
        setBirthDate(birthDate);
        setEmail(email);
        setPhone(phone);
        setDocument(document);
    }

    private void setUuid(UUID uuid) {
        this.uuid = requireNonNull(uuid);
    }

    private void setFullName(String fullName) {
        this.fullName = requireNonNull(StringUtils.trimToNull(fullName));
    }

    private void setBirthDate(LocalDate birthDate) {
        this.birthDate = requireNonNull(birthDate);
    }

    private void setEmail(String email) {
        this.email = requireValidEmail(email, EMAIL_IS_INVALID);
    }

    private void setPhone(String phone) {
        this.phone = requireNonNull(phone);
    }

    private void setDocument(String document) {
        this.document = requireNonNull(document);
    }

    private void setRegisteredAt(long registeredAt) {
        if (registeredAt <= 0) {
            throw new IllegalArgumentException(REGISTERED_AT_IS_INVALID);
        }
        this.registeredAt = registeredAt;
    }

    private void setPromotionNotificationsAllowed(boolean promotionNotificationsAllowed) {
        this.promotionNotificationsAllowed = promotionNotificationsAllowed;
    }

    private void setArchived(boolean archived) {
        this.archived = archived;
    }

    private void setArchivedAt(long archivedAt) {
        this.archivedAt = archivedAt;
    }

    private void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDocument() {
        return document;
    }

    public long getRegisteredAt() {
        return registeredAt;
    }

    public boolean isPromotionNotificationsAllowed() {
        return promotionNotificationsAllowed;
    }

    public boolean isArchived() {
        return archived;
    }

    public long getArchivedAt() {
        return archivedAt;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    // ============== Businnes methods bellow

    public void addLoyaltyPoints(int points) {
        setLoyaltyPoints(getLoyaltyPoints() + points);
    }

    public void archive() {
        setArchived(true);
        setArchivedAt(Instant.now().toEpochMilli());
        setFullName(ANONYMOUS_FULL_NAME);
        setPhone(ANONYMOUS_PHONE);
        setDocument(ANONYMOUS_DOCUMENT);
        setEmail(UUID.randomUUID() + ANONYMOUS_EMAIL_SUFFIX);
    }

    public void enablePromotionNotifications() {
        setPromotionNotificationsAllowed(true);
    }

    public void disablePromotionNotifications() {
        setPromotionNotificationsAllowed(false);
    }

    public void changeName(String newName) {
        setFullName(newName);
    }

    public void changeEmail(String newEmail) {
        setEmail(newEmail);
    }

    public void changePhone(String newPhone) {
        setPhone(newPhone);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

}
