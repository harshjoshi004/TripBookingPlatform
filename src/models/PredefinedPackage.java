package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PredefinedPackage extends Trip {
    private String packageName;
    private String description;
    private List<String> inclusions = new ArrayList<>();

    public PredefinedPackage(String destination, String origin, LocalDate departureDate,
                             LocalDate arrivalDate, int availableSeats, double basePrice,
                             String packageName, String description) {
        super(destination, origin, departureDate, arrivalDate, availableSeats, basePrice);
        this.packageName = packageName;
        this.description = description;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDescription() {
        return description;
    }

    public void addInclusion(String inclusion) {
        inclusions.add(inclusion);
    }

    public List<String> getInclusions() {
        return inclusions;
    }

    @Override
    public String toString() {
        return "Predefined Package: " + packageName +
                "\n" + super.toString() +
                "\nDescription: " + description +
                "\nInclusions: " + String.join(", ", inclusions);
    }
}
