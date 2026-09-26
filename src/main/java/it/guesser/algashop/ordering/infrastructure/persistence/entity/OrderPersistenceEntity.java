package it.guesser.algashop.ordering.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import it.guesser.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "\"order\"")
@Getter
@Setter
@ToString(of = "id")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private long id; // TSID

    private UUID customerId;

    private BigDecimal totalAmount;

    private Integer totalItems;

    private String status;

    private String paymentMethod;

    private long placedAt;

    private long paidAt;

    private long canceledAt;

    private long readyAt;

    private UUID createByUserId;

    private long lastModifiedAt;

    private UUID lastModifiedByUserId;

    @Version
    private long version;

    private BillingEmbeddable billing;

    private ShippingEmbeddable shipping;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemPersistencyEntity> items = new HashSet<>();

    private void setOrderOnItems() {
        getItems().forEach(item -> item.setOrder(this));
    }

    private void setItems(Set<OrderItemPersistencyEntity> items) {
        this.items = items;
    }

    public void replaceItems(Set<OrderItemPersistencyEntity> items) {
        if (CollectionUtils.isEmpty(items)) {
            setItems(new HashSet<>());
            return;
        }

        setItems(items);
        setOrderOnItems();
    }

    @Builder
    public OrderPersistenceEntity(long id, UUID customerId, BigDecimal totalAmount, Integer totalItems, String status,
                                  String paymentMethod, long placedAt, long paidAt, long canceledAt, long readyAt, UUID createByUserId,
                                  long lastModifiedAt, UUID lastModifiedByUserId, long version, BillingEmbeddable billing,
                                  ShippingEmbeddable shipping, Set<OrderItemPersistencyEntity> items) {
        this.id = id;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.placedAt = placedAt;
        this.paidAt = paidAt;
        this.canceledAt = canceledAt;
        this.readyAt = readyAt;
        this.createByUserId = createByUserId;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
        this.billing = billing;
        this.shipping = shipping;
        this.items = items == null ? new HashSet<>() : items;
        setOrderOnItems();
    }

    @PrePersist
    void prePersist() {
        // TODO: WHEN AUTHENTICATION IS DONE, ADJUST THAT TO GET USER FROM CONTEXT
        createByUserId = UUID.randomUUID();
        lastModifiedByUserId = createByUserId;
        lastModifiedAt = Instant.now().toEpochMilli();
    }

    @PreUpdate
    void preUpdate() {
        // TODO: WHEN AUTHENTICATION IS DONE, ADJUST THAT TO GET USER FROM CONTEXT
        lastModifiedByUserId = createByUserId;
        lastModifiedAt = Instant.now().toEpochMilli();
    }
}
