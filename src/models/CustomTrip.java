package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomTrip extends Trip {
    private Passenger creator;
    private List<String> specialRequests = new ArrayList<>();

    public CustomTrip(String destination, String origin, LocalDate departureDate,
                      LocalDate arrivalDate, int availableSeats, double basePrice,
                      Passenger creator) {
        super(destination, origin, departureDate, arrivalDate, availableSeats, basePrice);
        this.creator = creator;
    }

    public Passenger getCreator() {
        return creator;
    }

    public void addSpecialRequest(String request) {
        specialRequests.add(request);
    }

    public List<String> getSpecialRequests() {
        return specialRequests;
    }

    @Override
    public String toString() {
        return "Custom Trip: " +
                "\n" + super.toString() +
                "\nCreated by: " + creator.getName() +
                "\nSpecial Requests: " + String.join(", ", specialRequests);
    }
}
