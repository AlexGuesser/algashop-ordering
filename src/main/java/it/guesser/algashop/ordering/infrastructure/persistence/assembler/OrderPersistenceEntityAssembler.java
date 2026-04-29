package it.guesser.algashop.ordering.infrastructure.persistence.assembler;

import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import org.springframework.stereotype.Component;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

@Component
public class OrderPersistenceEntityAssembler {

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.getId().value().toLong());
        orderPersistenceEntity.setCustomerId(order.getCustomerId().value());
        orderPersistenceEntity.setStatus(order.getStatus().name());
        orderPersistenceEntity.setPaymentMethod(order.getPaymentMethod().name());
        orderPersistenceEntity.setTotalAmount(order.getTotalAmount().value());
        orderPersistenceEntity.setTotalItems(order.getTotalItems().value());
        orderPersistenceEntity.setCanceledAt(order.getCanceledAt());
        orderPersistenceEntity.setPaidAt(order.getPaidAt());
        orderPersistenceEntity.setPlacedAt(order.getPlacedAt());
        orderPersistenceEntity.setReadyAt(order.getReadyAt());
        orderPersistenceEntity.setVersion(order.getVersion());
        return orderPersistenceEntity;
    }

    private void mergeBillingFields(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        Billing billing = order.getBilling();

        if (billing == null) {
            orderPersistenceEntity.setBilling(null);
        }

//        BillingEmbeddable.builder()
//                .firstName(billing.f)
//                        .email(order.getBilling().email().value())
//                .
    }

}
