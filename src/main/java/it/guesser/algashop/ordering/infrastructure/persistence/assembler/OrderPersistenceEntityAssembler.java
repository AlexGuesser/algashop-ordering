package it.guesser.algashop.ordering.infrastructure.persistence.assembler;

import it.guesser.algashop.ordering.domain.entity.OrderItem;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistencyEntity;
import org.springframework.stereotype.Component;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.valueobject.Address;
import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.domain.valueobject.Recipient;
import it.guesser.algashop.ordering.domain.valueobject.Shipping;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import it.guesser.algashop.ordering.infrastructure.persistence.utils.FullNameUtil;

import java.util.Set;
import java.util.stream.Collectors;

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
        mergeBillingFields(orderPersistenceEntity, order);
        mergeShippingFields(orderPersistenceEntity, order);
        mergeItems(orderPersistenceEntity, order);
        return orderPersistenceEntity;
    }

    private void mergeItems(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        Set<OrderItemPersistencyEntity> persistenceEntities = order.getItems()
                .stream()
                .map(this::fromDomain)
                .collect(Collectors.toSet());

        orderPersistenceEntity.replaceItems(persistenceEntities);
    }

    public OrderItemPersistencyEntity fromDomain(OrderItem orderItem) {
        return merge(new OrderItemPersistencyEntity(), orderItem);
    }

    private OrderItemPersistencyEntity merge(OrderItemPersistencyEntity itemPersistence, OrderItem itemDomain) {
        itemPersistence.setId(itemDomain.getId().value().toLong());
        itemPersistence.setProductId(itemDomain.getProductId().value());
        itemPersistence.setProductName(itemDomain.getProductName().value());
        itemPersistence.setProductPrice(itemDomain.getProductPrice().value());
        itemPersistence.setQuantity(itemDomain.getQuantity().value());
        itemPersistence.setTotalAmount(itemDomain.getTotalAmount().value());
        return itemPersistence;
    }

    private void mergeBillingFields(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        Billing billing = order.getBilling();

        if (billing == null) {
            orderPersistenceEntity.setBilling(null);
            return;
        }

        Address address = billing.address();

        orderPersistenceEntity.setBilling(BillingEmbeddable.builder()
                .firstName(FullNameUtil.getFirstName(billing.fullName()).orElse(null))
                .lastName(FullNameUtil.getLastName(billing.fullName()).orElse(null))
                .document(billing.document().value())
                .phone(billing.phone().value())
                .email(billing.email().value())
                .address(AddressEmbeddable.builder()
                        .street(address.street())
                        .complement(address.complement())
                        .neighborhood(address.neighborhood())
                        .city(address.city())
                        .state(address.state())
                        .zipCode(address.zipCode().value())
                        .build())
                .build());
    }

    private void mergeShippingFields(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        Shipping shipping = order.getShipping();

        if (shipping == null) {
            orderPersistenceEntity.setShipping(null);
            return;
        }

        Recipient recipient = shipping.recipient();
        Address address = shipping.address();

        orderPersistenceEntity.setShipping(ShippingEmbeddable.builder()
                .cost(shipping.cost().value())
                .expectedDate(shipping.expectedDate())
                .recipient(RecipientEmbeddable.builder()
                        .firstName(FullNameUtil.getFirstName(recipient.fullName()).orElse(null))
                        .lastName(FullNameUtil.getLastName(recipient.fullName()).orElse(null))
                        .document(recipient.document().value())
                        .phone(recipient.phone().value())
                        .build())
                .address(AddressEmbeddable.builder()
                        .street(address.street())
                        .complement(address.complement())
                        .neighborhood(address.neighborhood())
                        .city(address.city())
                        .state(address.state())
                        .zipCode(address.zipCode().value())
                        .build())
                .build());
    }

}
