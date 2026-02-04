package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import it.guesser.algashop.ordering.domain.exceptions.CustomerAlreadyArchivedException;
import it.guesser.algashop.ordering.domain.exceptions.ErrorMessages;
import it.guesser.algashop.ordering.domain.valueobject.BirthDate;
import it.guesser.algashop.ordering.domain.valueobject.Document;
import it.guesser.algashop.ordering.domain.valueobject.Email;
import it.guesser.algashop.ordering.domain.valueobject.FullName;
import it.guesser.algashop.ordering.domain.valueobject.LoyaltyPoints;
import it.guesser.algashop.ordering.domain.valueobject.Phone;

public class CustomerTest {

    @Test
    void givenValidData_whenConstructingCustomer_thenFieldsAreInitializedCorrectly() {
        FullName fullName = new FullName("John Doe");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
        Email email = new Email("john.doe@example.com");
        Phone phone = new Phone("555-1234");
        Document document = new Document("DOC123");

        Customer customer = new Customer(fullName, birthDate, email, phone, document);

        assertThat(customer.getCustomerId()).isNotNull();
        assertThat(customer.getRegisteredAt()).isGreaterThan(0L);

        assertThat(customer.getFullName()).isEqualTo(fullName);
        assertThat(customer.getBirthDate()).isEqualTo(birthDate);
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getPhone()).isEqualTo(phone);
        assertThat(customer.getDocument()).isEqualTo(document);

        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
        assertThat(customer.isArchived()).isFalse();
        assertThat(customer.getArchivedAt()).isZero();
        assertThat(customer.getLoyaltyPoints().points()).isZero();
    }

    @Test
    void givenNullFullName_whenConstructingCustomer_thenThrowsNullPointerException() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
        Email email = new Email("john.doe@example.com");
        Phone phone = new Phone("555-1234");
        Document document = new Document("DOC123");

        assertThatThrownBy(() -> new Customer(null, birthDate, email, phone, document))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullBirthDate_whenConstructingCustomer_thenThrowsNullPointerException() {
        FullName fullName = new FullName("John Doe");
        Email email = new Email("john.doe@example.com");
        Phone phone = new Phone("555-1234");
        Document document = new Document("DOC123");

        assertThatThrownBy(() -> new Customer(fullName, null, email, phone, document))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullEmail_whenConstructingCustomer_thenThrowsNullPointerException() {
        FullName fullName = new FullName("John Doe");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
        Phone phone = new Phone("555-1234");
        Document document = new Document("DOC123");

        assertThatThrownBy(() -> new Customer(fullName, birthDate, null, phone, document))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullPhone_whenConstructingCustomer_thenThrowsNullPointerException() {
        FullName fullName = new FullName("John Doe");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
        Email email = new Email("john.doe@example.com");
        Document document = new Document("DOC123");

        assertThatThrownBy(() -> new Customer(fullName, birthDate, email, null, document))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullDocument_whenConstructingCustomer_thenThrowsNullPointerException() {
        FullName fullName = new FullName("John Doe");
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
        Email email = new Email("john.doe@example.com");
        Phone phone = new Phone("555-1234");

        assertThatThrownBy(() -> new Customer(fullName, birthDate, email, phone, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenCustomer_whenAddLoyaltyPoints_thenPointsIncrease() {
        Customer customer = createsTestCustomer();

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        assertThat(customer.getLoyaltyPoints().points()).isEqualTo(10);

        customer.addLoyaltyPoints(new LoyaltyPoints(5));
        assertThat(customer.getLoyaltyPoints().points()).isEqualTo(15);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1 })
    void givenCustomer_whenAddingInvalidLoyaltyPoints_thenExceptionIsThrown(
            int invalidLoyaltyPointsToAdd) {
        Customer customer = createsTestCustomer();

        assertThatThrownBy(
                () -> customer.addLoyaltyPoints(new LoyaltyPoints(invalidLoyaltyPointsToAdd)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenCustomer_whenArchive_thenCustomerIsMarkedAsArchivedAndPersonalDataIsAnonymized() {
        Customer customer = createsTestCustomer();

        customer.archive();

        assertThat(customer.isArchived()).isTrue();
        assertThat(customer.getArchivedAt()).isGreaterThan(0L);

        assertThat(customer.getFullName()).isEqualTo(new FullName("Anonymous"));
        assertThat(customer.getPhone()).isEqualTo(new Phone("000-000-0000"));
        assertThat(customer.getDocument()).isEqualTo(new Document("000-00-0000"));
        assertThat(customer.getEmail().value()).endsWith("@anonymous.com");
        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
    }

    @Test
    void givenCustomer_whenEnablePromotionNotifications_thenFlagIsSetToTrue() {
        Customer customer = createsTestCustomer();

        customer.enablePromotionNotifications();

        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();
    }

    @Test
    void givenCustomerWithPromotionNotificationsEnabled_whenDisablePromotionNotifications_thenFlagIsSetToFalse() {
        Customer customer = createsTestCustomer();

        customer.enablePromotionNotifications();
        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();

        customer.disablePromotionNotifications();

        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
    }

    @Test
    void givenCustomer_whenChangeName_thenFullNameIsUpdated() {
        Customer customer = createsTestCustomer();

        customer.changeName(new FullName("Jane Smith"));

        assertThat(customer.getFullName()).isEqualTo(new FullName("Jane Smith"));
    }

    @Test
    void givenCustomer_whenChangeEmail_thenEmailIsUpdated() {
        Customer customer = createsTestCustomer();

        customer.changeEmail(new Email("jane.smith@example.com"));

        assertThat(customer.getEmail()).isEqualTo(new Email("jane.smith@example.com"));
    }

    @Test
    void givenCustomer_whenChangePhone_thenPhoneIsUpdated() {
        Customer customer = createsTestCustomer();

        customer.changePhone(new Phone("999-8888"));

        assertThat(customer.getPhone()).isEqualTo(new Phone("999-8888"));
    }

    @Test
    void givenTwoCustomers_whenCompareEqualsAndHashCode_thenContractIsFollowed() {
        Customer customer1 = createsTestCustomer();
        Customer customer2 = createsTestCustomer();

        assertThat(customer1).isEqualTo(customer1);
        assertThat(customer1.hashCode()).isEqualTo(customer1.hashCode());

        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void givenCustomer_whenCompareWithNullAndDifferentTypes_thenEqualsHandlesProperly() {
        Customer customer = createsTestCustomer();

        assertThat(customer).isNotEqualTo(null);
        assertThat(customer).isNotEqualTo("some string");
        assertThat(customer).isEqualTo(customer);
    }

    @Test
    void givenAlreadyArchivedCustomer_whenTryingToUpdate_thenExceptionIsThrown() {
        Customer customer = createsTestCustomer();
        assertThat(customer.isArchived()).isFalse();
        customer.archive();
        assertThat(customer.isArchived()).isTrue();

        assertThatThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(10)))
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) ->
                                assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED));

        assertThatThrownBy(() -> customer.archive())
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) ->
                                assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED));

        assertThatThrownBy(() -> customer.enablePromotionNotifications())
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) ->
                                assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED));

        assertThatThrownBy(() -> customer.disablePromotionNotifications())
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) ->
                                assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED));

        assertThatThrownBy(() -> customer.changeName(new FullName("New name")))
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) ->
                                assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED));

        assertThatThrownBy(() -> customer.changeEmail(new Email("newEmail@email.com")))
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) ->
                                assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED));

        assertThatThrownBy(() -> customer.changePhone(new Phone("000-000")))
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) ->
                                assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED));
    }

    private Customer createsTestCustomer() {
        return new Customer(
                new FullName("John Doe"),
                new BirthDate(LocalDate.now()),
                new Email("john.doe@example.com"),
                new Phone("555-1234"),
                new Document("DOC123"));
    }
}
