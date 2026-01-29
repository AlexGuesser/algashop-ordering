package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    void constructorShouldInitializeFieldsCorrectly() {
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
    void addLoyaltyPointsShouldIncreasePoints() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.addLoyaltyPoints(10);
        assertThat(customer.getLoyaltyPoints()).isEqualTo(10);

        customer.addLoyaltyPoints(5);
        assertThat(customer.getLoyaltyPoints()).isEqualTo(15);
    }

    @Test
    void archiveShouldMarkCustomerAsArchivedAndSetArchivedAt() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.archive();

        assertThat(customer.isArchived()).isTrue();
        assertThat(customer.getArchivedAt()).isGreaterThan(0L);
    }

    @Test
    void enablePromotionNotificationsShouldSetFlagToTrue() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.enablePromotionNotifications();

        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();
    }

    @Test
    void disablePromotionNotificationsShouldSetFlagToFalse() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.enablePromotionNotifications();
        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();

        customer.disablePromotionNotifications();

        assertThat(customer.isPromotionNotificationsAllowed()).isFalse();
    }

    @Test
    void changeNameShouldUpdateFullName() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.changeName("Jane Smith");

        assertThat(customer.getFullName()).isEqualTo("Jane Smith");
    }

    @Test
    void changeEmailShouldUpdateEmail() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.changeEmail("jane.smith@example.com");

        assertThat(customer.getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    void changePhoneShouldUpdatePhone() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        customer.changePhone("999-8888");

        assertThat(customer.getPhone()).isEqualTo("999-8888");
    }

    @Test
    void equalsAndHashCodeShouldFollowContract() {
        Customer customer1 = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");
        Customer customer2 = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        // same instance
        assertThat(customer1).isEqualTo(customer1);
        assertThat(customer1.hashCode()).isEqualTo(customer1.hashCode());

        // different instances (different UUIDs) should normally not be equal
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void equalsShouldHandleNullAndDifferentTypes() {
        Customer customer = new Customer("John Doe", LocalDate.now(), "john.doe@example.com", "555-1234", "DOC123");

        assertThat(customer).isNotEqualTo(null);
        assertThat(customer).isNotEqualTo("some string");
        assertThat(customer).isEqualTo(customer);
    }
}
