package it.guesser.algashop.ordering.domain.repository;

import java.util.Optional;

import it.guesser.algashop.ordering.domain.entity.AggregateRoot;

public interface Repository<T extends AggregateRoot<ID>, ID> {

    Optional<T> ofId(ID id);

    boolean exists(ID id);

    void add(T aggregateRoot);

    long count();

}
