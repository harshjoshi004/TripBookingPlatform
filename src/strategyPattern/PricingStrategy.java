package strategyPattern;

import models.Trip;

public interface PricingStrategy {
    double calculatePrice(Trip trip);
}

