package it.guesser.algashop.ordering.infrastructure.persistence.provider;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.repository.Orders;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import it.guesser.algashop.ordering.infrastructure.persistence.disassembler.OrdemPersistenceEntityDisassembler;
import it.guesser.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository repository;
    private final OrderPersistenceEntityAssembler assembler;
    private final OrdemPersistenceEntityDisassembler disassembler;

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
        var persistenceEntity = assembler.fromDomain(aggregateRoot);

        repository.saveAndFlush(persistenceEntity);
    }

    @Override
    public long count() {
        return repository.count();
    }

}
