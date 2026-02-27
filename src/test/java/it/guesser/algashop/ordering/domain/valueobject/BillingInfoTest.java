package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class BillingInfoTest {

    private FullName fullName() {
        return new FullName("John Doe");
    }

    private Document document() {
        return new Document("12345678901");
    }

    private Phone phone() {
        return new Phone("555-1234");
    }

    private Address address() {
        return new Address(
                "Main Street",
                "Apt 101",
                "Downtown",
                "Metropolis",
                "ST",
                new ZipCode("12345"));
    }

    private Email email() {
        return new Email("email@gmail.com");
    }

    @Test
    void givenValidData_whenConstructingBillingInfo_thenFieldsAreSet() {
        Billing billingInfo = new Billing(fullName(), document(), phone(), address(), email());

        assertThat(billingInfo.fullName()).isEqualTo(fullName());
        assertThat(billingInfo.document()).isEqualTo(document());
        assertThat(billingInfo.phone()).isEqualTo(phone());
        assertThat(billingInfo.address()).isEqualTo(address());
    }

    @Test
    void givenNullFullName_whenConstructingBillingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Billing(null, document(), phone(), address(), email()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullDocument_whenConstructingBillingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Billing(fullName(), null, phone(), address(), email()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullPhone_whenConstructingBillingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Billing(fullName(), document(), null, address(), email()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullAddress_whenConstructingBillingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Billing(fullName(), document(), phone(), null, email()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullEmail_whenConstructingBillingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Billing(fullName(), document(), phone(), address(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSameValues_whenComparingBillingInfos_thenAreEqual() {
        Billing first = new Billing(fullName(), document(), phone(), address(), email());
        Billing second = new Billing(fullName(), document(), phone(), address(), email());

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
