package it.guesser.algashop.ordering.infrastructure.persistence.disassembler;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.domain.valueobject.Shipping;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

class OrderPersistenceEntityDisassemblerBillingShippingTest {

    private final OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    @Test
    void givenPersistenceEntityWithBillingAndShipping_whenToDomain_thenBillingAndShippingMapped() {
        BillingEmbeddable billing = BillingEmbeddable.builder()
                .firstName("John")
                .lastName("Doe")
                .document("12345678901")
                .phone("555-1234")
                .email("email@gmail.com")
                .address(AddressEmbeddable.builder()
                        .street("Main St")
                        .complement("Apt 1")
                        .neighborhood("Downtown")
                        .city("City")
                        .state("ST")
                        .zipCode("12345")
                        .build())
                .build();

        RecipientEmbeddable recipient = RecipientEmbeddable.builder()
                .firstName("John")
                .lastName("Doe")
                .document("12345678901")
                .phone("555-1234")
                .build();

        ShippingEmbeddable shipping = ShippingEmbeddable.builder()
                .cost(new BigDecimal("10.00"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .recipient(recipient)
                .address(AddressEmbeddable.builder()
                        .street("Main St")
                        .complement("Apt 1")
                        .neighborhood("Downtown")
                        .city("City")
                        .state("ST")
                        .zipCode("12345")
                        .build())
                .build();

        OrderPersistenceEntity entity = OrderPersistenceEntity.builder()
                .id(1L)
                .customerId(UUID.randomUUID())
                .totalAmount(new BigDecimal("100.00"))
                .totalItems(1)
                .status(OrderStatus.PAID.name())
                .paymentMethod(PaymentMethod.CREDIT_CARD.name())
                .placedAt(Instant.now().getEpochSecond())
                .paidAt(Instant.now().getEpochSecond())
                .canceledAt(0L)
                .readyAt(0L)
                .version(0L)
                .billing(billing)
                .shipping(shipping)
                .build();

        var domain = disassembler.toDomain(entity);

        Billing domainBilling = domain.getBilling();
        assertThat(domainBilling).isNotNull();
        assertThat(domainBilling.fullName().fullName()).isEqualTo("John Doe");
        assertThat(domainBilling.document().value()).isEqualTo("12345678901");
        assertThat(domainBilling.phone().value()).isEqualTo("555-1234");
        assertThat(domainBilling.email().value()).isEqualTo("email@gmail.com");
        assertThat(domainBilling.address().street()).isEqualTo("Main St");

        Shipping domainShipping = domain.getShipping();
        assertThat(domainShipping).isNotNull();
        assertThat(domainShipping.cost().value()).isEqualTo(new BigDecimal("10.00"));
        assertThat(domainShipping.expectedDate()).isEqualTo(shipping.getExpectedDate());
        assertThat(domainShipping.recipient().fullName().fullName()).isEqualTo("John Doe");
        assertThat(domainShipping.recipient().document().value()).isEqualTo("12345678901");
    }

}
