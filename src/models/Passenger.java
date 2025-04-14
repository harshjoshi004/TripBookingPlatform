package models;

import java.util.ArrayList;
import java.util.List;

public class Passenger extends User {
    private List<Booking> bookings = new ArrayList<>();

    public Passenger(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    @Override
    public String toString() {
        return "Passenger: " + super.toString();
    }
}
