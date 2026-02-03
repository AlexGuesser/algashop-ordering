package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSources;
import org.junit.jupiter.params.provider.ValueSource;

import it.guesser.algashop.ordering.domain.exceptions.CustomerAlreadyArchivedException;
import it.guesser.algashop.ordering.domain.exceptions.ErrorMessages;

public class CustomerTest {

    @Test
    void givenValidData_whenConstructingCustomer_thenFieldsAreInitializedCorrectly() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        Customer customer = new Customer("John Doe", birthDate, "john.doe@example.com", "555-1234", "DOC123");

        assertThat(customer.getUuid()).isNotNull();
        assertThat(customer.getRegisteredAt()).isGreaterThan(0L);

        assertThat(customer.getFullName()).isEqualTo("John Doe");
        assertThat(customer.getBirthDate()).isEqualTo(birthDate);
        assertThat(customer.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(customer.getPhone()).isEqualTo("555-1234");
        assertThat(customer.getDocument()).isEqualTo("DOC123");

        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
        assertThat(customer.isArchived()).isFalse();
        assertThat(customer.getArchivedAt()).isZero();
        assertThat(customer.getLoyaltyPoints()).isZero();
    }

    @Test
    void givenNullEmail_whenConstructingCustomer_thenThrowsNullPointerException() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        assertThatThrownBy(() -> new Customer("John Doe", birthDate, null, "555-1234", "DOC123"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankEmail_whenConstructingCustomer_thenThrowsNullPointerException() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        assertThatThrownBy(() -> new Customer("John Doe", birthDate, "   ", "555-1234", "DOC123"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenInvalidEmail_whenConstructingCustomer_thenThrowsIllegalArgumentException() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        assertThatThrownBy(() -> new Customer("John Doe", birthDate, "invalid-email", "555-1234", "DOC123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("E-mail is invalid");
    }

    @Test
    void givenNullFullName_whenConstructingCustomer_thenThrowsNullPointerException() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        assertThatThrownBy(() -> new Customer(null, birthDate, "john.doe@example.com", "555-1234", "DOC123"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankFullName_whenConstructingCustomer_thenThrowsNullPointerException() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        assertThatThrownBy(() -> new Customer("   ", birthDate, "john.doe@example.com", "555-1234", "DOC123"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullBirthDate_whenConstructingCustomer_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Customer("John Doe", null, "john.doe@example.com", "555-1234", "DOC123"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullPhone_whenConstructingCustomer_thenThrowsNullPointerException() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        assertThatThrownBy(() -> new Customer("John Doe", birthDate, "john.doe@example.com", null, "DOC123"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullDocument_whenConstructingCustomer_thenThrowsNullPointerException() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        assertThatThrownBy(() -> new Customer("John Doe", birthDate, "john.doe@example.com", "555-1234", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenCustomer_whenAddLoyaltyPoints_thenPointsIncrease() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.addLoyaltyPoints(10);
        assertThat(customer.getLoyaltyPoints()).isEqualTo(10);

        customer.addLoyaltyPoints(5);
        assertThat(customer.getLoyaltyPoints()).isEqualTo(15);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void givenCustomer_whenAddingInvalidLoyaltyPoints_thenExceptionIsThrown(
            int invalidLoyaltyPointsToAdd) {
        Customer customer = createsTestCustomer();

        assertThatThrownBy(
                () -> customer.addLoyaltyPoints(invalidLoyaltyPointsToAdd))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenCustomer_whenArchive_thenCustomerIsMarkedAsArchivedAndPersonalDataIsAnonymized() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.archive();

        assertThat(customer.isArchived()).isTrue();
        assertThat(customer.getArchivedAt()).isGreaterThan(0L);

        assertThat(customer.getFullName()).isEqualTo("Anonymous");
        assertThat(customer.getPhone()).isEqualTo("000-000-0000");
        assertThat(customer.getDocument()).isEqualTo("000-00-0000");
        assertThat(customer.getEmail()).endsWith("@anonymous.com");
        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
    }

    @Test
    void givenCustomer_whenEnablePromotionNotifications_thenFlagIsSetToTrue() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.enablePromotionNotifications();

        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();
    }

    @Test
    void givenCustomerWithPromotionNotificationsEnabled_whenDisablePromotionNotifications_thenFlagIsSetToFalse() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.enablePromotionNotifications();
        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();

        customer.disablePromotionNotifications();

        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
    }

    @Test
    void givenCustomer_whenChangeName_thenFullNameIsUpdated() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.changeName("Jane Smith");

        assertThat(customer.getFullName()).isEqualTo("Jane Smith");
    }

    @Test
    void givenCustomer_whenChangeEmail_thenEmailIsUpdated() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.changeEmail("jane.smith@example.com");

        assertThat(customer.getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    void givenCustomer_whenChangePhone_thenPhoneIsUpdated() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.changePhone("999-8888");

        assertThat(customer.getPhone()).isEqualTo("999-8888");
    }

    @Test
    void givenTwoCustomers_whenCompareEqualsAndHashCode_thenContractIsFollowed() {
        Customer customer1 = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");
        Customer customer2 = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        // same instance
        assertThat(customer1).isEqualTo(customer1);
        assertThat(customer1.hashCode()).isEqualTo(customer1.hashCode());

        // different instances (different UUIDs) should normally not be equal
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void givenCustomer_whenCompareWithNullAndDifferentTypes_thenEqualsHandlesProperly() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        assertThat(customer).isNotEqualTo(null);
        assertThat(customer).isNotEqualTo("some string");
        assertThat(customer).isEqualTo(customer);
    }

    @Test
    void giverAlreadyArchivedCustomer_whenTryingToUpdate_shouldGenerateException() {
        Customer customer = createsTestCustomer();
        assertThat(customer.isArchived()).isFalse();
        customer.archive();
        assertThat(customer.isArchived()).isTrue();

        assertThatThrownBy(
                () -> customer.addLoyaltyPoints(10))
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) -> {
                            assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED);
                        });

        assertThatThrownBy(
                () -> customer.archive())
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) -> {
                            assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED);
                        });

        assertThatThrownBy(
                () -> customer.enablePromotionNotifications())
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) -> {
                            assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED);
                        });

        assertThatThrownBy(
                () -> customer.disablePromotionNotifications())
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) -> {
                            assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED);
                        });

        assertThatThrownBy(
                () -> customer.changeName("New name"))
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) -> {
                            assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED);
                        });

        assertThatThrownBy(
                () -> customer.changeEmail("newEmail@email.com"))
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) -> {
                            assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED);
                        });

        assertThatThrownBy(
                () -> customer.changePhone("000-000"))
                .isInstanceOfSatisfying(
                        CustomerAlreadyArchivedException.class,
                        (exception) -> {
                            assertThat(exception.getMessage()).isEqualTo(ErrorMessages.CUSTOMER_ALREADY_ARCHIVED);
                        });

    }

    private Customer createsTestCustomer() {
        return new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");
    }
}
