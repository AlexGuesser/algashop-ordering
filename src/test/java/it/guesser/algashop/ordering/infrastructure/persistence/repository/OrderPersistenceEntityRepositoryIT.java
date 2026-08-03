package it.guesser.algashop.ordering.infrastructure.persistence.repository;

import static it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder.anOrderPersistenceEntity;
import static it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder.existingItem;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;

import it.guesser.algashop.ordering.AbstractDataJpaIntegrationTest;
import it.guesser.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(SpringDataAuditingConfig.class)
public class OrderPersistenceEntityRepositoryIT extends AbstractDataJpaIntegrationTest {

    private final OrderPersistenceEntityRepository repository;

    @Autowired
    public OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository repository) {
        this.repository = repository;
    }

    @Test
    void shouldPersist() {
        OrderPersistenceEntity order = anOrderPersistenceEntity()
                .withItem(existingItem())
                .build();
        assertThat(repository.count()).isZero();

        repository.saveAndFlush(order);

        OrderPersistenceEntity savedEntity = repository.getReferenceById(order.getId());

        assertThat(savedEntity.getItems()).isNotEmpty();
    }

    @Test
    void shouldSetAuditingValues() {
        OrderPersistenceEntity order = anOrderPersistenceEntity().build();

        OrderPersistenceEntity savedOrder = repository.saveAndFlush(order);

        assertThat(savedOrder.getCreateByUserId()).isNotNull();
        assertThat(savedOrder.getLastModifiedAt()).isGreaterThan(0);
        assertThat(savedOrder.getLastModifiedByUserId()).isNotNull();
    }

}
