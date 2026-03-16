package it.guesser.algashop.ordering.domain.repository;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.OrderTestDataBuilder;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import it.guesser.algashop.ordering.infrastructure.persistence.disassembler.OrdemPersistenceEntityDisassembler;
import it.guesser.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;

@DataJpaTest
@Import({ OrdersPersistenceProvider.class, OrderPersistenceEntityAssembler.class,
        OrdemPersistenceEntityDisassembler.class })
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

}
