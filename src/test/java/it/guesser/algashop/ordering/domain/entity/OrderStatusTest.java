package it.guesser.algashop.ordering.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class OrderStatusTest {

    @Test
    void givenDraft_whenCheckAllowedTransitions_thenCanGoToPlacedOrCanceledOnly() {
        Set<OrderStatus> transitions = OrderStatus.DRAFT.allowedTransitions();

        assertThat(transitions)
                .containsExactlyInAnyOrder(OrderStatus.PLACED, OrderStatus.CANCELED);
    }

    @Test
    void givenPlaced_whenCheckAllowedTransitions_thenCanGoToPaidOrCanceledOnly() {
        Set<OrderStatus> transitions = OrderStatus.PLACED.allowedTransitions();

        assertThat(transitions)
                .containsExactlyInAnyOrder(OrderStatus.PAID, OrderStatus.CANCELED);
    }

    @Test
    void givenPaid_whenCheckAllowedTransitions_thenCanGoToReadyOrCanceledOnly() {
        Set<OrderStatus> transitions = OrderStatus.PAID.allowedTransitions();

        assertThat(transitions)
                .containsExactlyInAnyOrder(OrderStatus.READY, OrderStatus.CANCELED);
    }

    @Test
    void givenReady_whenCheckAllowedTransitions_thenCanGoToCanceledOnly() {
        Set<OrderStatus> transitions = OrderStatus.READY.allowedTransitions();

        assertThat(transitions)
                .containsExactly(OrderStatus.CANCELED);
    }

    @Test
    void givenCanceled_whenCheckAllowedTransitions_thenNoMoreTransitionsAreAllowed() {
        Set<OrderStatus> transitions = OrderStatus.CANCELED.allowedTransitions();

        assertThat(transitions).isEmpty();
    }

    @Test
    void givenStatus_whenCheckCanChangeTo_thenMatchesAllowedTransitions() {
        assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED)).isTrue();
        assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELED)).isTrue();
        assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PAID)).isFalse();
        assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.READY)).isFalse();

        assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.PAID)).isTrue();
        assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.CANCELED)).isTrue();
        assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.DRAFT)).isFalse();
        assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.READY)).isFalse();

        assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.READY)).isTrue();
        assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.CANCELED)).isTrue();
        assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.DRAFT)).isFalse();
        assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.PLACED)).isFalse();

        assertThat(OrderStatus.READY.canChangeTo(OrderStatus.CANCELED)).isTrue();
        assertThat(OrderStatus.READY.canChangeTo(OrderStatus.DRAFT)).isFalse();
        assertThat(OrderStatus.READY.canChangeTo(OrderStatus.PLACED)).isFalse();
        assertThat(OrderStatus.READY.canChangeTo(OrderStatus.PAID)).isFalse();

        assertThat(OrderStatus.CANCELED.canChangeTo(OrderStatus.DRAFT)).isFalse();
        assertThat(OrderStatus.CANCELED.canChangeTo(OrderStatus.PLACED)).isFalse();
        assertThat(OrderStatus.CANCELED.canChangeTo(OrderStatus.PAID)).isFalse();
        assertThat(OrderStatus.CANCELED.canChangeTo(OrderStatus.READY)).isFalse();
    }
}

