package it.guesser.algashop.ordering.domain.entity;

import static it.guesser.algashop.ordering.domain.exceptions.ErrorMessages.REGISTERED_AT_IS_INVALID;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.UUID;

import it.guesser.algashop.ordering.domain.exceptions.CustomerAlreadyArchivedException;
import it.guesser.algashop.ordering.domain.valueobject.Address;
import it.guesser.algashop.ordering.domain.valueobject.BirthDate;
import it.guesser.algashop.ordering.domain.valueobject.Document;
import it.guesser.algashop.ordering.domain.valueobject.Email;
import it.guesser.algashop.ordering.domain.valueobject.FullName;
import it.guesser.algashop.ordering.domain.valueobject.LoyaltyPoints;
import it.guesser.algashop.ordering.domain.valueobject.Phone;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;

public class Customer {

    private static final FullName ANONYMOUS_FULL_NAME = new FullName("Anonymous");
    private static final Phone ANONYMOUS_PHONE = new Phone("000-000-0000");
    private static final Document ANONYMOUS_DOCUMENT = new Document("000-00-0000");
    private static final String ANONYMOUS_EMAIL_SUFFIX = "@anonymous.com";

    private CustomerId customerId;
    private FullName fullName;
    private BirthDate birthDate;
    private Email email;
    private Phone phone;
    private Document document;
    private long registeredAt;
    private boolean promotionNotificationsAllowed;
    private boolean archived;
    private long archivedAt;
    private LoyaltyPoints loyaltyPoints;
    private Address address;

    public static Customer brandNew(
            FullName fullName,
            BirthDate birthDate,
            Email email,
            Phone phone,
            Document document,
            Address address) {
        return new Customer(
                new CustomerId(),
                fullName,
                birthDate,
                email,
                phone,
                document,
                Instant.now().toEpochMilli(), // registered at
                false,
                false,
                0,
                LoyaltyPoints.ZERO,
                address);
    }

    public static Customer existent(
            CustomerId customerId,
            FullName fullName,
            BirthDate birthDate,
            Email email,
            Phone phone,
            Document document,
            long registeredAt,
            boolean promotionNotificationsAllowed,
            boolean archived,
            long archivedAt,
            LoyaltyPoints loyaltyPoints,
            Address address) {
        return new Customer(customerId, fullName, birthDate, email, phone, document, registeredAt,
                promotionNotificationsAllowed, archived, archivedAt, loyaltyPoints, address);
    }

    private Customer(
            CustomerId customerId,
            FullName fullName,
            BirthDate birthDate,
            Email email,
            Phone phone,
            Document document,
            long registeredAt,
            boolean promotionNotificationsAllowed,
            boolean archived,
            long archivedAt,
            LoyaltyPoints loyaltyPoints,
            Address address) {
        setCustomerId(customerId);
        setFullName(fullName);
        setBirthDate(birthDate);
        setEmail(email);
        setPhone(phone);
        setDocument(document);
        setRegisteredAt(registeredAt);
        setPromotionNotificationsAllowed(promotionNotificationsAllowed);
        setArchived(archived);
        setArchivedAt(archivedAt);
        setLoyaltyPoints(loyaltyPoints);
        setAddress(address);
    }

    private void setCustomerId(CustomerId customerId) {
        this.customerId = requireNonNull(customerId);
    }

    private void setFullName(FullName fullName) {
        this.fullName = requireNonNull(fullName);
    }

    private void setBirthDate(BirthDate birthDate) {
        this.birthDate = requireNonNull(birthDate);
    }

    private void setEmail(Email email) {
        this.email = requireNonNull(email);
    }

    private void setPhone(Phone phone) {
        this.phone = requireNonNull(phone);
    }

    private void setDocument(Document document) {
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

    private void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
        this.loyaltyPoints = requireNonNull(loyaltyPoints);
    }

    private void setAddress(Address address) {
        this.address = requireNonNull(address);
    }

    private void verifyIfChangeable() {
        if (isArchived()) {
            throw new CustomerAlreadyArchivedException();
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public FullName getFullName() {
        return fullName;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    public Email getEmail() {
        return email;
    }

    public Phone getPhone() {
        return phone;
    }

    public Document getDocument() {
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

    public LoyaltyPoints getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public Address getAddress() {
        return address;
    }

    // ============== Businnes methods bellow

    public void addLoyaltyPoints(LoyaltyPoints loyaltyPointsToAdd) {
        verifyIfChangeable();
        setLoyaltyPoints(getLoyaltyPoints().add(loyaltyPointsToAdd));
    }

    public void archive() {
        verifyIfChangeable();
        setArchived(true);
        setArchivedAt(Instant.now().toEpochMilli());
        setFullName(ANONYMOUS_FULL_NAME);
        setPhone(ANONYMOUS_PHONE);
        setDocument(ANONYMOUS_DOCUMENT);
        setEmail(new Email(UUID.randomUUID() + ANONYMOUS_EMAIL_SUFFIX));
        setPromotionNotificationsAllowed(false);
        setAddress(address.anonymize());
    }

    public void enablePromotionNotifications() {
        verifyIfChangeable();
        setPromotionNotificationsAllowed(true);
    }

    public void disablePromotionNotifications() {
        verifyIfChangeable();
        setPromotionNotificationsAllowed(false);
    }

    public void changeName(FullName newName) {
        verifyIfChangeable();
        setFullName(newName);
    }

    public void changeEmail(Email newEmail) {
        verifyIfChangeable();
        setEmail(newEmail);
    }

    public void changePhone(Phone newPhone) {
        verifyIfChangeable();
        setPhone(newPhone);
    }

    public void changeAddress(Address address) {
        verifyIfChangeable();
        setAddress(address);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
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
        if (customerId == null) {
            if (other.customerId != null)
                return false;
        } else if (!customerId.equals(other.customerId))
            return false;
        return true;
    }

}
