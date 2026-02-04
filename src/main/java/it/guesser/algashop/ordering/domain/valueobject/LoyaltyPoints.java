package it.guesser.algashop.ordering.domain.valueobject;

import static java.util.Objects.requireNonNull;

public record LoyaltyPoints(int points) implements Comparable<LoyaltyPoints> {

    public static final LoyaltyPoints ZERO = new LoyaltyPoints(0);

    public LoyaltyPoints() {
        this(0);
    }

    public LoyaltyPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException();
        }
        this.points = points;
    }

    public LoyaltyPoints add(LoyaltyPoints pointsToAdd) {
        requireNonNull(pointsToAdd);
        int newAmountOfPoints = this.points() + pointsToAdd.points();
        return new LoyaltyPoints(newAmountOfPoints);
    }

    public LoyaltyPoints substract(LoyaltyPoints pointsToSubstract) {
        requireNonNull(pointsToSubstract);
        int newAmountOfPoints = this.points() - pointsToSubstract.points();
        return new LoyaltyPoints(newAmountOfPoints);
    }

    @Override
    public int compareTo(LoyaltyPoints o) {
        return Integer.valueOf(this.points()).compareTo(Integer.valueOf(o.points()));
    }

}
