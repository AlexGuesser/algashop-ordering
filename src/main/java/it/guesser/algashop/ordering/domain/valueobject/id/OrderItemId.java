package it.guesser.algashop.ordering.domain.valueobject.id;

import static java.util.Objects.requireNonNull;

import io.hypersistence.tsid.TSID;
import it.guesser.algashop.ordering.domain.utils.IdGenerator;

public record OrderItemId(TSID value) {

    public OrderItemId {
        requireNonNull(value);
    }

    public OrderItemId() {
        this(IdGenerator.generateTSID());
    }

    public OrderItemId(Long value) {
        this(TSID.from(requireNonNull(value)));
    }

    public OrderItemId(String value) {
        this(TSID.from(requireNonNull(value)));
    }

}
