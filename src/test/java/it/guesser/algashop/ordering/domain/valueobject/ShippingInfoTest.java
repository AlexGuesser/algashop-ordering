package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ShippingInfoTest {

    private FullName fullName() {
        return new FullName("Jane Smith");
    }

    private Document document() {
        return new Document("98765432109");
    }

    private Phone phone() {
        return new Phone("555-5678");
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
        ShippingInfo shippingInfo = new ShippingInfo(fullName(), document(), phone(), address());

        assertThat(shippingInfo.fullName()).isEqualTo(fullName());
        assertThat(shippingInfo.document()).isEqualTo(document());
        assertThat(shippingInfo.phone()).isEqualTo(phone());
        assertThat(shippingInfo.address()).isEqualTo(address());
    }

    @Test
    void givenNullFullName_whenConstructingShippingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new ShippingInfo(null, document(), phone(), address()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullDocument_whenConstructingShippingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new ShippingInfo(fullName(), null, phone(), address()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullPhone_whenConstructingShippingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new ShippingInfo(fullName(), document(), null, address()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullAddress_whenConstructingShippingInfo_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new ShippingInfo(fullName(), document(), phone(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSameValues_whenComparingShippingInfos_thenAreEqual() {
        ShippingInfo first = new ShippingInfo(fullName(), document(), phone(), address());
        ShippingInfo second = new ShippingInfo(fullName(), document(), phone(), address());

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
