package it.guesser.algashop.ordering.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
}
