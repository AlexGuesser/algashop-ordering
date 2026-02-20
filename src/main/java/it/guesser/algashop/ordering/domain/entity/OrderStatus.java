package it.guesser.algashop.ordering.domain.entity;

import java.util.Set;

public enum OrderStatus {
    DRAFT {
        @Override
        public Set<OrderStatus> allowedTransitions() {
            return Set.of(PLACED, CANCELED);
        }
    },
    PLACED {
        @Override
        public Set<OrderStatus> allowedTransitions() {
            return Set.of(PAID, CANCELED);
        }
    },
    PAID {
        @Override
        public Set<OrderStatus> allowedTransitions() {
            return Set.of(READY, CANCELED);
        }
    },
    READY {
        @Override
        public Set<OrderStatus> allowedTransitions() {
            return Set.of(CANCELED);
        }
    },
    CANCELED {
        @Override
        public Set<OrderStatus> allowedTransitions() {
            return Set.of();
        }
    };

    public abstract Set<OrderStatus> allowedTransitions();

    public boolean canChangeTo(OrderStatus next) {
        return allowedTransitions().contains(next);
    }

    public boolean canNotChangeTo(OrderStatus next) {
        return !canChangeTo(next);
    }

}