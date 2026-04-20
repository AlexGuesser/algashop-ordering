package it.guesser.algashop.ordering.domain.repository;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.OrderTestDataBuilder;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import it.guesser.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import it.guesser.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;

@DataJpaTest
@Import({OrdersPersistenceProvider.class, OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class})
public class OrdersIT {

    private final Orders orders;

    @Autowired
    public OrdersIT(Orders orders) {
        this.orders = requireNonNull(orders);
    }

    @Test
    void shouldPersistAndFind() {
        Order order = OrderTestDataBuilder.anOrder().withStatus(OrderStatus.DRAFT).build();
        OrderId orderId = order.getId();

        orders.add(order);
        Optional<Order> possibleOrder = orders.ofId(orderId);

        assertThat(possibleOrder).isPresent();
        assertThat(possibleOrder.get()).satisfies(
                o -> assertThat(o.getId()).isEqualTo(orderId),
                o -> assertThat(o.getCustomerId()).isEqualTo(order.getCustomerId()));
    }

    @Test
    void shouldUpdateExistingOrder() {
        Order order = OrderTestDataBuilder.anOrder().withStatus(OrderStatus.PLACED).build();

        orders.add(order);

        order = orders.ofId(order.getId()).orElseThrow();

        order.markAsPaid();

        orders.add(order);

        order = orders.ofId(order.getId()).orElseThrow();

        assertThat(order.isPaid()).isTrue();
    }

    @Test
    void shouldNotAllowStaleUpdates() {
        Order order = OrderTestDataBuilder.anOrder().withStatus(OrderStatus.PLACED).build();
        orders.add(order);

        Order orderT1 = orders.ofId(order.getId()).orElseThrow();
        Order orderT2 = orders.ofId(order.getId()).orElseThrow();

        orderT1.markAsPaid();
        orders.add(orderT1);

        orderT2.markAsCanceled();
        orders.add(orderT2);

        Order savedOrder = orders.ofId(order.getId()).orElseThrow();
        Assertions.assertThat(savedOrder.getPaidAt()).isGreaterThan(0);
        Assertions.assertThat(savedOrder.getCanceledAt()).isGreaterThan(0);
    }

}
