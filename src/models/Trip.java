package models;

import observerPattern.Observable;
import observerPattern.Observer;
import strategyPattern.PricingStrategy;
import strategyPattern.RegularSeasonPricing;
import tripDecorator.TripComponent;
import java.time.LocalDate;
import java.util.*;
import observerPattern.*;

public abstract class Trip implements Observable {
    private String tripId;
    private String destination;
    private String origin;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private int availableSeats;
    private double basePrice;
    private PricingStrategy pricingStrategy;
    private List<Observer> observers = new ArrayList<>();
    private TripComponent itinerary;

    public Trip(String destination, String origin, LocalDate departureDate,
                LocalDate arrivalDate, int availableSeats, double basePrice) {
        this.tripId = UUID.randomUUID().toString();
        this.destination = destination;
        this.origin = origin;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.availableSeats = availableSeats;
        this.basePrice = basePrice;
        this.pricingStrategy = new RegularSeasonPricing(); // Default strategy
    }

    public String getTripId() {
        return tripId;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
        return origin;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int seats) {
        this.availableSeats = seats;
        notifyObservers();
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double calculatePrice() {
        return pricingStrategy.calculatePrice(this);
    }

    public void setItinerary(TripComponent itinerary) {
        this.itinerary = itinerary;
    }

    public TripComponent getItinerary() {
        return itinerary;
    }

    // Observer pattern implementation
    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public String toString() {
        return "Trip ID: " + tripId +
                ", From: " + origin +
                ", To: " + destination +
                ", Departure: " + departureDate +
                ", Arrival: " + arrivalDate +
                ", Available Seats: " + availableSeats +
                ", Price: $" + calculatePrice();
    }
}
