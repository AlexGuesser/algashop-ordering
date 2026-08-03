package it.guesser.algashop.ordering.infrastructure.persistence.assembler;

import static it.guesser.algashop.ordering.domain.entity.OrderTestDataBuilder.anOrder;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.entity.Order;
import it.guesser.algashop.ordering.domain.entity.OrderStatus;
import it.guesser.algashop.ordering.domain.entity.PaymentMethod;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

class OrderPersistenceEntityAssemblerBillingShippingTest {

    private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

    @Test
    void givenOrderWithBilling_whenFromDomain_thenBillingFieldsMappedCorrectly() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).build();

        OrderPersistenceEntity entity = assembler.fromDomain(order);

        assertThat(entity.getBilling()).satisfies(b -> {
            assertThat(b.getFirstName()).isEqualTo("John");
            assertThat(b.getLastName()).isEqualTo("Doe");
            assertThat(b.getDocument()).isEqualTo("12345678901");
            assertThat(b.getPhone()).isEqualTo("555-1234");
            assertThat(b.getEmail()).isEqualTo("email@gmail.com");

            assertThat(b.getAddress()).satisfies(a -> {
                assertThat(a.getStreet()).isEqualTo("Main St");
                assertThat(a.getComplement()).isEqualTo("Apt 1");
                assertThat(a.getNeighborhood()).isEqualTo("Downtown");
                assertThat(a.getCity()).isEqualTo("City");
                assertThat(a.getState()).isEqualTo("ST");
                assertThat(a.getZipCode()).isEqualTo("12345");
            });
        });
    }

    @Test
    void givenOrderWithShipping_whenFromDomain_thenShippingFieldsMappedCorrectly() {
        Order order = anOrder().withStatus(OrderStatus.DRAFT).build();

        OrderPersistenceEntity entity = assembler.fromDomain(order);

        assertThat(entity.getShipping()).satisfies(s -> {
            assertThat(s.getCost()).isEqualTo(order.getShipping().cost().value());
            assertThat(s.getExpectedDate()).isEqualTo(order.getShipping().expectedDate());

            assertThat(s.getRecipient()).satisfies(r -> {
                assertThat(r.getFirstName()).isEqualTo("John");
                assertThat(r.getLastName()).isEqualTo("Doe");
                assertThat(r.getDocument()).isEqualTo("12345678901");
                assertThat(r.getPhone()).isEqualTo("555-1234");
            });

            assertThat(s.getAddress()).satisfies(a -> {
                assertThat(a.getStreet()).isEqualTo("Main St");
                assertThat(a.getComplement()).isEqualTo("Apt 1");
                assertThat(a.getNeighborhood()).isEqualTo("Downtown");
                assertThat(a.getCity()).isEqualTo("City");
                assertThat(a.getState()).isEqualTo("ST");
                assertThat(a.getZipCode()).isEqualTo("12345");
            });
        });
    }

    @Test
    void givenOrderWithoutBilling_whenFromDomain_thenBillingIsNull() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE);

        OrderPersistenceEntity entity = assembler.fromDomain(order);

        assertThat(entity.getBilling()).isNull();
    }

    @Test
    void givenOrderWithoutShipping_whenFromDomain_thenShippingIsNull() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE);

        OrderPersistenceEntity entity = assembler.fromDomain(order);

        assertThat(entity.getShipping()).isNull();
    }

}
