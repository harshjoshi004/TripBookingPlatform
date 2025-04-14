package strategyPattern;

import models.Trip;

public class PeakSeasonPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Trip trip) {
        return trip.getBasePrice() * 1.3; // 30% markup
    }
}