package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ShippingTest {

    private FullName fullName() {
        return new FullName("Jane Smith");
    }

    private Document document() {
        return new Document("98765432109");
    }

    private Phone phone() {
        return new Phone("555-5678");
    }

    private Recipient recipient() {
        return new Recipient(fullName(), document(), phone());
    }

    private Money cost() {
        return Money.ZERO;
    }

    private LocalDate expectedDate() {
        return LocalDate.now().plusWeeks(1);
    }

    private Address address() {
        return new Address(
                "Oak Avenue",
                null,
                "Suburbs",
                "Smallville",
                "CA",
                new ZipCode("54321"));
    }

    @Test
    void givenValidData_whenConstructingShippingInfo_thenFieldsAreSet() {
        Shipping shippingInfo = new Shipping(cost(), expectedDate(), recipient(), address());

        assertThat(shippingInfo.cost()).isEqualTo(cost());
        assertThat(shippingInfo.expectedDate()).isEqualTo(expectedDate());
        assertThat(shippingInfo.recipient()).isEqualTo(recipient());
        assertThat(shippingInfo.address()).isEqualTo(address());
    }

    @Test
    void givenNullCost_whenConstructingShippingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Shipping(null, expectedDate(), recipient(), address()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullExpectedDate_whenConstructingShippingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Shipping(cost(), null, recipient(), address()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullRecipient_whenConstructingShippingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Shipping(cost(), expectedDate(), null, address()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullAddress_whenConstructingShippingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Shipping(cost(), expectedDate(), recipient(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSameValues_whenComparingShippingInfos_thenAreEqual() {
        Shipping first = new Shipping(cost(), expectedDate(), recipient(), address());
        Shipping second = new Shipping(cost(), expectedDate(), recipient(), address());

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
