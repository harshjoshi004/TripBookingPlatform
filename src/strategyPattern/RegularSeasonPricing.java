package strategyPattern;

import models.Trip;

public class RegularSeasonPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Trip trip) {
        return trip.getBasePrice();
    }
}
