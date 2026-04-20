package it.guesser.algashop.ordering.infrastructure.persistence.disassembler;

import java.util.Set;

import org.springframework.stereotype.Component;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomain(OrderPersistenceEntity persistenceEntity) {
        return Order.ofExistent(
                new OrderId(persistenceEntity.getId()),
                new CustomerId(persistenceEntity.getCustomerId()),
                new Money(persistenceEntity.getTotalAmount()),
                new Quantity(persistenceEntity.getTotalItems()),
                persistenceEntity.getPlacedAt(),
                persistenceEntity.getPaidAt(),
                persistenceEntity.getCanceledAt(),
                persistenceEntity.getReadyAt(),
                null,
                null,
                OrderStatus.valueOf(persistenceEntity.getStatus()),
                PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()),
                Set.of());
    }

}
