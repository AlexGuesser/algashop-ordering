package it.guesser.algashop.ordering.infrastructure.persistence.disassembler;

import java.util.Set;
import java.util.stream.Collectors;

import it.guesser.algashop.ordering.domain.entity.OrderItem;
import it.guesser.algashop.ordering.domain.valueobject.*;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderItemId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistencyEntity;
import org.springframework.stereotype.Component;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import it.guesser.algashop.ordering.infrastructure.persistence.utils.FullNameUtil;

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
                toBilling(persistenceEntity.getBilling()),
                toShipping(persistenceEntity.getShipping()),
                OrderStatus.valueOf(persistenceEntity.getStatus()),
                PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()),
                toItems(persistenceEntity.getItems()),
                persistenceEntity.getVersion());
    }

    private Set<OrderItem> toItems(Set<OrderItemPersistencyEntity> items) {
        return items.stream()
                .map(item -> OrderItem.ofExistent(
                        new OrderItemId(item.getId()),
                        new OrderId(item.getOrderId()),
                        new ProductId(item.getProductId()),
                        new ProductName(item.getProductName()),
                        new Money(item.getProductPrice()),
                        new Quantity(item.getQuantity()),
                        new Money(item.getTotalAmount())
                )).collect(Collectors.toSet());
    }

    private Billing toBilling(BillingEmbeddable billingEmbeddable) {
        if (billingEmbeddable == null) {
            return null;
        }

        return new Billing(
                new FullName(
                        FullNameUtil.getFullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName())),
                new Document(billingEmbeddable.getDocument()),
                new Phone(billingEmbeddable.getPhone()),
                new Address(billingEmbeddable.getAddress().getStreet(), billingEmbeddable.getAddress().getComplement(),
                        billingEmbeddable.getAddress().getNeighborhood(), billingEmbeddable.getAddress().getCity(),
                        billingEmbeddable.getAddress().getState(),
                        new ZipCode(billingEmbeddable.getAddress().getZipCode())),
                new Email(billingEmbeddable.getEmail()));
    }

    private Shipping toShipping(ShippingEmbeddable shippingEmbeddable) {
        if (shippingEmbeddable == null) {
            return null;
        }

        return new Shipping(
                new Money(shippingEmbeddable.getCost()),
                shippingEmbeddable.getExpectedDate(),
                new Recipient(
                        new FullName(
                                FullNameUtil.getFullName(
                                        shippingEmbeddable.getRecipient().getFirstName(),
                                        shippingEmbeddable.getRecipient().getLastName())),
                        new Document(shippingEmbeddable.getRecipient().getDocument()),
                        new Phone(shippingEmbeddable.getRecipient().getPhone())),
                new Address(
                        shippingEmbeddable.getAddress().getStreet(),
                        shippingEmbeddable.getAddress().getComplement(),
                        shippingEmbeddable.getAddress().getNeighborhood(),
                        shippingEmbeddable.getAddress().getCity(),
                        shippingEmbeddable.getAddress().getState(),
                        new ZipCode(shippingEmbeddable.getAddress().getZipCode())));
    }
}
