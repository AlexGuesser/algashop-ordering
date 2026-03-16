package it.guesser.algashop.ordering.infrastructure.persistence.disassembler;

import static it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder.anOrderPersistenceEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

class OrdemPersistenceEntityDisassemblerTest {

    private final OrdemPersistenceEntityDisassembler disassembler = new OrdemPersistenceEntityDisassembler();

    @Test
    void givenOrderPersistenceEntity_whenToDomain_thenAllFieldsAreMappedCorrectly() {
        OrderPersistenceEntity entity = anOrderPersistenceEntity()
                .withStatus(OrderStatus.PAID.name())
                .withPaymentMethod(PaymentMethod.CREDIT_CARD)
                .build();

        Order order = disassembler.toDomain(entity);

        assertThat(order).satisfies(o -> {
            assertThat(o.getId().value().toLong()).isEqualTo(entity.getId());
            assertThat(o.getCustomerId().value()).isEqualTo(entity.getCustomerId());
            assertThat(o.getTotalAmount().value()).isEqualTo(entity.getTotalAmount());
            assertThat(o.getTotalItems().value()).isEqualTo(entity.getTotalItems());
            assertThat(o.getPlacedAt()).isEqualTo(entity.getPlacedAt());
            assertThat(o.getPaidAt()).isEqualTo(entity.getPaidAt());
            assertThat(o.getCanceledAt()).isEqualTo(entity.getCanceledAt());
            assertThat(o.getReadyAt()).isEqualTo(entity.getReadyAt());
            assertThat(o.getStatus()).isEqualTo(OrderStatus.valueOf(entity.getStatus()));
            assertThat(o.getPaymentMethod()).isEqualTo(PaymentMethod.valueOf(entity.getPaymentMethod()));
        });
    }
}

