package models;

import enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Booking {
    private String bookingId;
    private User user;
    private Trip trip;
    private LocalDateTime bookingTime;
    private BookingStatus status;

    public Booking(User user, Trip trip) {
        this.bookingId = UUID.randomUUID().toString();
        this.user = user;
        this.trip = trip;
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Trip getTrip() {
        return trip;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
                ", User: " + user.getName() +
                ", Trip: " + trip.getDestination() +
                ", Status: " + status;
    }
}
