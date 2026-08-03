package it.guesser.algashop.ordering.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderId;
import it.guesser.algashop.ordering.domain.valueobject.id.OrderItemId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * OrderItemPersistencyEntity
 */
@Entity
@Table(name = "order_item")
@Getter
@Setter
@ToString(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemPersistencyEntity {

    @Id
    @EqualsAndHashCode.Include
    private long id; // TSID

    private UUID productId;

    private String productName;

    private BigDecimal productPrice;

    private int quantity;

    private BigDecimal totalAmount;

    @JoinColumn
    @ManyToOne(optional = false)
    private OrderPersistenceEntity order;

    public long getOrderId() {
        return getOrder().getId();
    }



}
