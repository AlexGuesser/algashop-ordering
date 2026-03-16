package it.guesser.algashop.ordering.infrastructure.persistence.repository;

import static it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder.anOrderPersistenceEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;

import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderPersistenceEntityRepositoryIT {

    private final OrderPersistenceEntityRepository repository;

    @Autowired
    public OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository repository) {
        this.repository = repository;
    }

    @Test
    void shouldPersist() {
        OrderPersistenceEntity order = anOrderPersistenceEntity()
                .build();
        assertThat(repository.count()).isZero();

        repository.saveAndFlush(order);

        assertThat(repository.existsById(order.getId())).isTrue();
    }

}
