package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    void givenValidData_whenConstructingAddress_thenFieldsAreNormalized() {
        Address address = new Address(
                "  Main Street  ",
                "   ",
                "  Downtown  ",
                "  Metropolis  ",
                "  ST  ",
                new ZipCode("12345"));

        assertThat(address.street()).isEqualTo("Main Street");
        assertThat(address.complement()).isNull();
        assertThat(address.neighborhood()).isEqualTo("Downtown");
        assertThat(address.city()).isEqualTo("Metropolis");
        assertThat(address.state()).isEqualTo("ST");
        assertThat(address.zipCode()).isEqualTo(new ZipCode("12345"));
    }

    @Test
    void givenNullOrBlankRequiredFields_whenConstructingAddress_thenThrowsNullPointerException() {
        ZipCode zipCode = new ZipCode("12345");

        assertThatThrownBy(() -> new Address(null, "comp", "neighborhood", "city", "state", zipCode))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Address("   ", "comp", "neighborhood", "city", "state", zipCode))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Address("street", "comp", null, "city", "state", zipCode))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Address("street", "comp", "   ", "city", "state", zipCode))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Address("street", "comp", "neighborhood", null, "state", zipCode))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Address("street", "comp", "neighborhood", "   ", "state", zipCode))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Address("street", "comp", "neighborhood", "city", null, zipCode))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Address("street", "comp", "neighborhood", "city", "   ", zipCode))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Address("street", "comp", "neighborhood", "city", "state", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenAddress_whenAnonymize_thenOnlyStreetAndComplementAreAnonymized() {
        Address original = new Address(
                "Main Street",
                "Apt 101",
                "Downtown",
                "Metropolis",
                "ST",
                new ZipCode("12345"));

        Address anonymized = original.anonymize();

        assertThat(anonymized).isNotEqualTo(original);
        assertThat(anonymized.street()).isEqualTo("anonymous");
        assertThat(anonymized.complement()).isEqualTo("anonymous");
        assertThat(anonymized.neighborhood()).isEqualTo(original.neighborhood());
        assertThat(anonymized.city()).isEqualTo(original.city());
        assertThat(anonymized.state()).isEqualTo(original.state());
        assertThat(anonymized.zipCode()).isEqualTo(original.zipCode());
    }
}

