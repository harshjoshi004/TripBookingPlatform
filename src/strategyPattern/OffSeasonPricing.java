package strategyPattern;

import models.Trip;

public class OffSeasonPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Trip trip) {
        return trip.getBasePrice() * 0.8; // 20% discount
    }
}