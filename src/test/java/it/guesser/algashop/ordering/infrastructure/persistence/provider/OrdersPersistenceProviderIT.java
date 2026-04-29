package it.guesser.algashop.ordering.infrastructure.persistence.provider;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.OrderTestDataBuilder;
import it.guesser.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import it.guesser.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import it.guesser.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import it.guesser.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({OrdersPersistenceProvider.class, OrderPersistenceEntityAssembler.class, OrderPersistenceEntityDisassembler.class, SpringDataAuditingConfig.class})
class OrdersPersistenceProviderIT {

    private OrdersPersistenceProvider ordersPersistenceProvider;
    private OrderPersistenceEntityRepository orderPersistenceEntityRepository;

    @Autowired
    public OrdersPersistenceProviderIT(OrdersPersistenceProvider ordersPersistenceProvider, OrderPersistenceEntityRepository orderPersistenceEntityRepository) {
        this.ordersPersistenceProvider = ordersPersistenceProvider;
        this.orderPersistenceEntityRepository = orderPersistenceEntityRepository;
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Order order = OrderTestDataBuilder.anOrder().withStatus(OrderStatus.PLACED).build();
        long orderId = order.getId().value().toLong();
        ordersPersistenceProvider.save(order);

        var persistenceEntity = orderPersistenceEntityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PLACED.name());

        Assertions.assertThat(persistenceEntity.getCreateByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isGreaterThan(0);
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        order = ordersPersistenceProvider.ofId(order.getId()).orElseThrow();
        order.markAsPaid();
        ordersPersistenceProvider.save(order);

        persistenceEntity = orderPersistenceEntityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());

        Assertions.assertThat(persistenceEntity.getCreateByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isGreaterThan(0);
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }

}