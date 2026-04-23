package it.guesser.algashop.ordering.infrastructure.persistence.provider;

import java.util.Optional;

import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.repository.Orders;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import it.guesser.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import it.guesser.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository repository;
    private final OrderPersistenceEntityAssembler assembler;
    private final OrderPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<Order> ofId(OrderId id) {
        return repository.findById(id.value().toLong())
                .map(disassembler::toDomain);
    }

    @Override
    public boolean exists(OrderId id) {
        return ofId(id).isPresent();
    }

    @Override
    public void add(Order aggregateRoot) {
        long orderId = aggregateRoot.getId().value().toLong();

        repository.findById(orderId).ifPresentOrElse(
                //UPDATE
                (persistenceEntity) -> update(aggregateRoot, persistenceEntity),
                // INSERT
                () -> insert(aggregateRoot)

        );


    }

    private void update(Order aggregateRoot, OrderPersistenceEntity entity) {
        OrderPersistenceEntity persistenceEntity = assembler.merge(entity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        repository.saveAndFlush(persistenceEntity);
        aggregateRoot.changeVersion(persistenceEntity.getVersion());
    }

    private void insert(Order aggregateRoot) {
        var persistenceEntity = assembler.fromDomain(aggregateRoot);
        repository.saveAndFlush(persistenceEntity);
        aggregateRoot.changeVersion(persistenceEntity.getVersion());
    }

    @Override
    public long count() {
        return repository.count();
    }

}
