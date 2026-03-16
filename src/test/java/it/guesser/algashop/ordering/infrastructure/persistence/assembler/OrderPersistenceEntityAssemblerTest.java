package it.guesser.algashop.ordering.infrastructure.persistence.assembler;

import static it.guesser.algashop.ordering.domain.entity.OrderTestDataBuilder.anOrder;
import static it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder.anOrderPersistenceEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

class OrderPersistenceEntityAssemblerTest {

    private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

    @Test
    void givenPaidOrder_whenFromDomain_thenAllFieldsAreMappedCorrectly() {
        Order order = anOrder()
                .withStatus(OrderStatus.PAID)
                .build();

        OrderPersistenceEntity entity = assembler.fromDomain(order);

        assertThat(entity).satisfies(e -> {
            assertThat(e.getId()).isEqualTo(order.getId().value().toLong());
            assertThat(e.getCustomerId()).isEqualTo(order.getCustomerId().value());
            assertThat(e.getStatus()).isEqualTo(order.getStatus().name());
            assertThat(e.getPaymentMethod()).isEqualTo(order.getPaymentMethod().name());
            assertThat(e.getTotalAmount()).isEqualTo(order.getTotalAmount().value());
            assertThat(e.getTotalItems()).isEqualTo(order.getTotalItems().value());
            assertThat(e.getCanceledAt()).isEqualTo(order.getCanceledAt());
            assertThat(e.getPaidAt()).isEqualTo(order.getPaidAt());
            assertThat(e.getPlacedAt()).isEqualTo(order.getPlacedAt());
            assertThat(e.getReadyAt()).isEqualTo(order.getReadyAt());
        });
    }

    @Test
    void givenExistingPersistenceEntity_whenMerge_thenSameInstanceUpdatedWithOrderValues() {
        Order existingOrder = anOrder()
                .withStatus(OrderStatus.DRAFT)
                .build();

        OrderPersistenceEntity existingEntity = anOrderPersistenceEntity()
                .build();

        OrderPersistenceEntity mergedEntity = assembler.merge(existingEntity, existingOrder);

        assertThat(mergedEntity).isSameAs(existingEntity);

        assertThat(mergedEntity).satisfies(e -> {
            assertThat(e.getId()).isEqualTo(existingOrder.getId().value().toLong());
            assertThat(e.getCustomerId()).isEqualTo(existingOrder.getCustomerId().value());
            assertThat(e.getStatus()).isEqualTo(existingOrder.getStatus().name());
            assertThat(e.getPaymentMethod()).isEqualTo(existingOrder.getPaymentMethod().name());
            assertThat(e.getTotalAmount()).isEqualTo(existingOrder.getTotalAmount().value());
            assertThat(e.getTotalItems()).isEqualTo(existingOrder.getTotalItems().value());
            assertThat(e.getCanceledAt()).isEqualTo(existingOrder.getCanceledAt());
            assertThat(e.getPaidAt()).isEqualTo(existingOrder.getPaidAt());
            assertThat(e.getPlacedAt()).isEqualTo(existingOrder.getPlacedAt());
            assertThat(e.getReadyAt()).isEqualTo(existingOrder.getReadyAt());
        });
    }
}

