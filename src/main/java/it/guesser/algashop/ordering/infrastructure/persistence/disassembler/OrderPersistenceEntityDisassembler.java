package it.guesser.algashop.ordering.infrastructure.persistence.disassembler;

import java.util.Set;

import org.springframework.stereotype.Component;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.domain.valueobject.Address;
import it.guesser.algashop.ordering.domain.valueobject.Billing;
import it.guesser.algashop.ordering.domain.valueobject.Document;
import it.guesser.algashop.ordering.domain.valueobject.Email;
import it.guesser.algashop.ordering.domain.valueobject.FullName;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Phone;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.ZipCode;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
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
                null,
                OrderStatus.valueOf(persistenceEntity.getStatus()),
                PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()),
                Set.of(),
                persistenceEntity.getVersion());
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
}
