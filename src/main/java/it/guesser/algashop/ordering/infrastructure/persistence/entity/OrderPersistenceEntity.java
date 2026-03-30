package it.guesser.algashop.ordering.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"order\"")
@Getter
@Setter
@ToString(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
