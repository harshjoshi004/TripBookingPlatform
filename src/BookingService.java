import enums.BookingStatus;
import models.Booking;
import models.Passenger;
import models.Trip;
import models.User;
import observerPattern.PassengerObserver;

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private List<Booking> bookings = new ArrayList<>();
    private TripService tripService;

    public BookingService(TripService tripService) {
        this.tripService = tripService;
    }

    public Booking createBooking(User user, String tripId) {
        Trip trip = tripService.getTripById(tripId);
        if (trip == null) {
            return null;
        }

        if (trip.getAvailableSeats() > 0) {
            Booking booking = new Booking(user, trip);
            bookings.add(booking);

            // Update trip availability
            tripService.updateTripAvailability(tripId, 1);

            // If user is a passenger, add booking to their list
            if (user instanceof Passenger) {
                Passenger passenger = (Passenger) user;
                passenger.addBooking(booking);

                // Add passenger as observer for the trip
                PassengerObserver observer = new PassengerObserver(passenger);
                trip.addObserver(observer);
            }

            return booking;
        }

        return null;
    }

    public boolean cancelBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking != null && booking.getStatus() != BookingStatus.CANCELLED) {
            booking.cancel();

            // Update trip availability
            Trip trip = booking.getTrip();
            trip.setAvailableSeats(trip.getAvailableSeats() + 1);

            return true;
        }
        return false;
    }

    public List<Booking> getBookingsByUser(String userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUser().getUserId().equals(userId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public Booking getBookingById(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return booking;
            }
        }
        return null;
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }
}

