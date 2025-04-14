import models.CustomTrip;
import models.Passenger;
import models.PredefinedPackage;
import models.Trip;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripService {
    private List<Trip> trips = new ArrayList<>();

    public List<Trip> searchTrips(String origin, String destination, LocalDate date) {
        List<Trip> result = new ArrayList<>();
        for (Trip trip : trips) {
            if (trip.getOrigin().equalsIgnoreCase(origin) &&
                    trip.getDestination().equalsIgnoreCase(destination) &&
                    (trip.getDepartureDate().equals(date) ||
                            (trip.getDepartureDate().isBefore(date) &&
                                    trip.getArrivalDate().isAfter(date)
                            )
                    )
            ) {
                result.add(trip);
            }
        }
        return result;
    }

    public Trip getTripById(String tripId) {
        for (Trip trip : trips) {
            if (trip.getTripId().equals(tripId)) {
                return trip;
            }
        }
        return null;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    public boolean updateTripAvailability(String tripId, int seatsBooked) {
        Trip trip = getTripById(tripId);
        if (trip != null && trip.getAvailableSeats() >= seatsBooked) {
            trip.setAvailableSeats(trip.getAvailableSeats() - seatsBooked);
            return true;
        }
        return false;
    }

    public PredefinedPackage createPredefinedPackage(String destination, String origin,
                                                     LocalDate departureDate, LocalDate arrivalDate,
                                                     int availableSeats, String packageName, String description) {
        PredefinedPackage trip = new PredefinedPackage(destination, origin, departureDate,
                arrivalDate, availableSeats, 500.0, // Base price
                packageName, description);
        trips.add(trip);
        return trip;
    }

    public CustomTrip createCustomTrip(String destination, String origin,
                                       LocalDate departureDate, LocalDate arrivalDate,
                                       int availableSeats, Passenger creator) {
        CustomTrip trip = new CustomTrip(destination, origin, departureDate,
                arrivalDate, availableSeats, 300.0, // Base price
                creator);
        trips.add(trip);
        return trip;
    }

    public List<Trip> getAllTrips() {
        return trips;
    }

    public List<Trip> getCustomTripsByPassenger(Passenger passenger) {
        List<Trip> result = new ArrayList<>();
        for (Trip trip : trips) {
            if (trip instanceof CustomTrip) {
                CustomTrip customTrip = (CustomTrip) trip;
                if (customTrip.getCreator().getUserId().equals(passenger.getUserId())) {
                    result.add(trip);
                }
            }
        }
        return result;
    }

    public boolean removeTrip(String tripId) {
        Trip trip = getTripById(tripId);
        if (trip != null) {
            trips.remove(trip);
            return true;
        }
        return false;
    }
}

