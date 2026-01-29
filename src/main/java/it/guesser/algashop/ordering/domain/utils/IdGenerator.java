package it.guesser.algashop.ordering.domain.utils;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;

public class IdGenerator {

    private static final TimeBasedEpochRandomGenerator TIME_BASED_EPOCH_RANDOM_GENERATOR = Generators
            .timeBasedEpochRandomGenerator();

    private IdGenerator() {

    }

    public static UUID generateTimeBasedUuid() {
        return TIME_BASED_EPOCH_RANDOM_GENERATOR.generate();
    }

}
