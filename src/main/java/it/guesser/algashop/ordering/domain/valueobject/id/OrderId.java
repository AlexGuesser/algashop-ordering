package it.guesser.algashop.ordering.domain.valueobject.id;

import static java.util.Objects.requireNonNull;

import io.hypersistence.tsid.TSID;
import it.guesser.algashop.ordering.domain.utils.IdGenerator;

public record OrderId(TSID value) {

    public OrderId {
        requireNonNull(value);
    }

    public OrderId() {
        this(IdGenerator.generateTSID());
    }

    public OrderId(Long value) {
        this(TSID.from(requireNonNull(value)));
    }

    public OrderId(String value) {
        this(TSID.from(requireNonNull(value)));
    }

}
