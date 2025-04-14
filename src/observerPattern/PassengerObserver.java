package observerPattern;

import models.Passenger;
import models.Trip;

public class PassengerObserver implements Observer {
    private Passenger passenger;

    public PassengerObserver(Passenger passenger) {
        this.passenger = passenger;
    }

    @Override
    public void update(Trip trip) {
        System.out.println("NOTIFICATION to " + passenger.getName() +
                ": The trip to " + trip.getDestination() +
                " has been updated. Available seats: " + trip.getAvailableSeats());
    }
}
