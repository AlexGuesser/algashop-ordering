package it.guesser.algashop.ordering.infrastructure.persistence.entity;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.domain.utils.IdGenerator;

public class OrderPersistenceEntityTestDataBuilder {

    private Long id = IdGenerator.generateTSID().toLong();
    private UUID customerId = IdGenerator.generateTimeBasedUuid();
    private BigDecimal totalAmount = new BigDecimal("1000.00");
    private Integer totalItems = 2;
    private String status = "DRAFT";
    private String paymentMethod = PaymentMethod.GATEWAY_BALANCE.name();
    private long placedAt = Instant.now().getEpochSecond();
    private long paidAt = 0L;
    private long canceledAt = 0L;
    private long readyAt = 0L;

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntityTestDataBuilder anOrderPersistenceEntity() {
        return new OrderPersistenceEntityTestDataBuilder();
    }

    public OrderPersistenceEntityTestDataBuilder withId(long id) {
        this.id = requireNonNull(id);
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withCustomerId(UUID customerId) {
        this.customerId = requireNonNull(customerId);
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = requireNonNull(totalAmount);
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withTotalItems(Integer totalItems) {
        this.totalItems = requireNonNull(totalItems);
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withStatus(String status) {
        this.status = requireNonNull(status);
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = requireNonNull(paymentMethod).name();
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withPlacedAt(long placedAt) {
        this.placedAt = placedAt;
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withPaidAt(long paidAt) {
        this.paidAt = paidAt;
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withCanceledAt(long canceledAt) {
        this.canceledAt = canceledAt;
        return this;
    }

    public OrderPersistenceEntityTestDataBuilder withReadyAt(long readyAt) {
        this.readyAt = readyAt;
        return this;
    }

    public OrderPersistenceEntity build() {
        return OrderPersistenceEntity.builder()
                .id(id)
                .customerId(customerId)
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .status(status)
                .paymentMethod(paymentMethod)
                .placedAt(placedAt)
                .paidAt(paidAt)
                .canceledAt(canceledAt)
                .readyAt(readyAt)
                .build();
    }
}

