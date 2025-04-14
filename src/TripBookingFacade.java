import enums.BookingStatus;
import models.*;
import serviceDecorator.*;
import strategyPattern.OffSeasonPricing;
import strategyPattern.PeakSeasonPricing;
import strategyPattern.RegularSeasonPricing;
import tripDecorator.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TripBookingFacade {
    private static Scanner scanner = new Scanner(System.in);
    private static TripService tripService = new TripService();
    private static UserService userService = new UserService();
    private static BookingService bookingService = new BookingService(tripService);
    private static User currentUser = null;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        // Initialize with some data
        initializeData();

        System.out.println("Welcome to Trip Booking System!");
        System.out.println("==============================");

        // Main application loop
        boolean exit = false;
        while (!exit) {
            if (currentUser == null) {
                exit = loginMenu();
            } else if (currentUser instanceof Admin) {
                adminMenu();
            } else if (currentUser instanceof Passenger) {
                passengerMenu();
            }
        }

        System.out.println("\nThank you for using Trip Booking System!");
        scanner.close();
    }

    private static void initializeData() {
        // Create sample users
        Admin admin = userService.registerAdmin("Admin User", "admin@example.com", "1234567890");
        Passenger john = userService.registerPassenger("John Doe", "john@example.com", "9876543210");
        Passenger jane = userService.registerPassenger("Jane Smith", "jane@example.com", "5555555555");

        // Create sample trips
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusDays(7);
        LocalDate nextMonth = today.plusMonths(1);

        // Create predefined package
        PredefinedPackage parisTrip = tripService.createPredefinedPackage(
                "Paris", "London", tomorrow, nextWeek, 20, "Paris Explorer", "A wonderful week in Paris"
        );
        parisTrip.addInclusion("5-star hotel accommodation");
        parisTrip.addInclusion("Daily breakfast and dinner");
        parisTrip.addInclusion("Eiffel Tower tour");

        // Create another predefined package
        PredefinedPackage tokyoTrip = tripService.createPredefinedPackage(
                "Tokyo", "New York", tomorrow.plusDays(5), nextWeek.plusDays(10), 15, "Tokyo Adventure", "Experience Japan's capital"
        );
        tokyoTrip.addInclusion("4-star hotel accommodation");
        tokyoTrip.addInclusion("Tokyo Tower visit");
        tokyoTrip.addInclusion("Guided city tour");

        // Create custom trip
        CustomTrip romeTrip = tripService.createCustomTrip(
                "Rome", "Berlin", nextWeek, nextMonth, 5, john
        );
        romeTrip.addSpecialRequest("Vegetarian meals");
        romeTrip.addSpecialRequest("Airport pickup");

        // Add itineraries using decorator pattern
        TripComponent londonCity = new CityVisit("London", 100);
        TripComponent withParis = new BusTransport(londonCity, "London", "Paris", 50);
        parisTrip.setItinerary(withParis);

        TripComponent nyCity = new CityVisit("New York", 150);
        TripComponent withTokyo = new FlightTransport(nyCity, "New York", "Tokyo", 800);
        tokyoTrip.setItinerary(withTokyo);

        TripComponent berlinCity = new CityVisit("Berlin", 120);
        TripComponent withRome = new FlightTransport(berlinCity, "Berlin", "Rome", 180);
        romeTrip.setItinerary(withRome);

        // Create sample bookings
        bookingService.createBooking(john, parisTrip.getTripId());
    }

    private static boolean loginMenu() {
        System.out.println("\n===== Login Menu =====");
        System.out.println("1. Login as Admin");
        System.out.println("2. Login as Passenger");
        System.out.println("3. Register as New Passenger");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                loginAsAdmin();
                return false;
            case 2:
                loginAsPassenger();
                return false;
            case 3:
                registerPassenger();
                return false;
            case 4:
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }

    private static void loginAsAdmin() {
        List<User> admins = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            if (user instanceof Admin) {
                admins.add(user);
            }
        }

        if (admins.isEmpty()) {
            System.out.println("No admin users found. Please register an admin first.");
            return;
        }

        System.out.println("\n=== Admin Users ===");
        for (int i = 0; i < admins.size(); i++) {
            System.out.println((i + 1) + ". " + admins.get(i).getName() + " (" + admins.get(i).getEmail() + ")");
        }

        System.out.print("Select an admin (1-" + admins.size() + "): ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < admins.size()) {
            currentUser = admins.get(index);
            System.out.println("Logged in as " + currentUser.getName());
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private static void loginAsPassenger() {
        List<User> passengers = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            if (user instanceof Passenger) {
                passengers.add(user);
            }
        }

        if (passengers.isEmpty()) {
            System.out.println("No passenger users found. Please register a passenger first.");
            return;
        }

        System.out.println("\n=== Passenger Users ===");
        for (int i = 0; i < passengers.size(); i++) {
            System.out.println((i + 1) + ". " + passengers.get(i).getName() + " (" + passengers.get(i).getEmail() + ")");
        }

        System.out.print("Select a passenger (1-" + passengers.size() + "): ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < passengers.size()) {
            currentUser = passengers.get(index);
            System.out.println("Logged in as " + currentUser.getName());
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private static void registerPassenger() {
        System.out.println("\n=== Register New Passenger ===");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        Passenger passenger = userService.registerPassenger(name, email, phone);
        System.out.println("Passenger registered successfully!");

        // Automatically log in as the new passenger
        currentUser = passenger;
        System.out.println("Logged in as " + currentUser.getName());
    }

    private static void adminMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Create Predefined Package");
            System.out.println("2. View All Trips");
            System.out.println("3. Update Trip");
            System.out.println("4. Cancel Trip");
            System.out.println("5. View All Bookings");
            System.out.println("6. Manage Pricing Strategies");
            System.out.println("7. Log Out");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    createPredefinedPackage();
                    break;
                case 2:
                    viewAllTrips();
                    break;
                case 3:
                    updateTrip();
                    break;
                case 4:
                    cancelTrip();
                    break;
                case 5:
                    viewAllBookings();
                    break;
                case 6:
                    managePricingStrategies();
                    break;
                case 7:
                    currentUser = null;
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void passengerMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== Passenger Menu =====");
            System.out.println("1. Search Trips");
            System.out.println("2. View Available Predefined Packages");
            System.out.println("3. Book a Predefined Package");
            System.out.println("4. Create Custom Trip");
            System.out.println("5. View My Bookings");
            System.out.println("6. View My Custom Trips");
            System.out.println("7. Cancel a Booking");
            System.out.println("8. Log Out");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    searchTrips();
                    break;
                case 2:
                    viewPredefinedPackages();
                    break;
                case 3:
                    bookPredefinedPackage();
                    break;
                case 4:
                    createCustomTrip();
                    break;
                case 5:
                    viewMyBookings();
                    break;
                case 6:
                    viewMyCustomTrips();
                    break;
                case 7:
                    cancelBooking();
                    break;
                case 8:
                    currentUser = null;
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ===== ADMIN METHODS =====

    private static void createPredefinedPackage() {
        System.out.println("\n===== Create Predefined Package =====");

        System.out.print("Enter origin: ");
        String origin = scanner.nextLine();

        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        LocalDate departureDate = null;
        while (departureDate == null) {
            System.out.print("Enter departure date (YYYY-MM-DD): ");
            String input = scanner.nextLine();
            try {
                departureDate = LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        LocalDate arrivalDate = null;
        while (arrivalDate == null) {
            System.out.print("Enter arrival date (YYYY-MM-DD): ");
            String input = scanner.nextLine();
            try {
                arrivalDate = LocalDate.parse(input, dateFormatter);
                if (arrivalDate.isBefore(departureDate)) {
                    System.out.println("Arrival date cannot be before departure date.");
                    arrivalDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        System.out.print("Enter available seats: ");
        int availableSeats = getIntInput();

        // Package details
        System.out.print("Enter package name: ");
        String packageName = scanner.nextLine();

        System.out.print("Enter package description: ");
        String description = scanner.nextLine();

        Trip trip = tripService.createPredefinedPackage(destination, origin, departureDate,
                arrivalDate, availableSeats, packageName, description);

        // Add inclusions
        boolean addMoreInclusions = true;
        while (addMoreInclusions) {
            System.out.print("Add an inclusion (or type 'done' to finish): ");
            String inclusion = scanner.nextLine();

            if (inclusion.equalsIgnoreCase("done")) {
                addMoreInclusions = false;
            } else {
                ((PredefinedPackage) trip).addInclusion(inclusion);
                System.out.println("Inclusion added.");
            }
        }

        // Create a simple itinerary using the decorator pattern
        TripComponent originCity = new CityVisit(origin, 100);
        TripComponent withDestination = new BusTransport(originCity, origin, destination, 50);
        trip.setItinerary(withDestination);

        System.out.println("Predefined Package created successfully! Trip ID: " + trip.getTripId());
    }

    private static void viewAllTrips() {
        List<Trip> trips = tripService.getAllTrips();

        if (trips.isEmpty()) {
            System.out.println("No trips available.");
            return;
        }

        System.out.println("\n===== All Trips =====");
        int index = 1;
        for (Trip trip : trips) {
            System.out.println(index + ". " + trip);

            // Show itinerary if available
            if (trip.getItinerary() != null) {
                System.out.println("   Itinerary: " + trip.getItinerary().getDescription());
            }

            if (trip instanceof PredefinedPackage) {
                PredefinedPackage pkg = (PredefinedPackage) trip;
                System.out.println("   Package: " + pkg.getPackageName());
                System.out.println("   Description: " + pkg.getDescription());
                System.out.println("   Inclusions: " + String.join(", ", pkg.getInclusions()));
            } else if (trip instanceof CustomTrip) {
                CustomTrip customTrip = (CustomTrip) trip;
                System.out.println("   Created by: " + customTrip.getCreator().getName());
                System.out.println("   Special Requests: " + String.join(", ", customTrip.getSpecialRequests()));
            }

            System.out.println("   Price: $" + trip.calculatePrice());
            System.out.println();
            index++;
        }
    }

    private static void updateTrip() {
        List<Trip> trips = tripService.getAllTrips();

        if (trips.isEmpty()) {
            System.out.println("No trips available to update.");
            return;
        }

        System.out.println("\n===== Update Trip =====");
        System.out.println("Select a trip to update:");

        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            System.out.println((i + 1) + ". " + trip.getDestination() + " from " + trip.getOrigin() +
                    " (" + trip.getDepartureDate() + " to " + trip.getArrivalDate() + ")");
        }

        System.out.print("Enter trip number (1-" + trips.size() + "): ");
        int tripIndex = getIntInput() - 1;

        if (tripIndex < 0 || tripIndex >= trips.size()) {
            System.out.println("Invalid trip selection.");
            return;
        }

        Trip selectedTrip = trips.get(tripIndex);

        System.out.println("\nUpdate options for trip to " + selectedTrip.getDestination() + ":");
        System.out.println("1. Update available seats");
        System.out.println("2. Update pricing strategy");
        System.out.print("Enter choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                System.out.print("Enter new available seats: ");
                int seats = getIntInput();

                if (seats < 0) {
                    System.out.println("Seats cannot be negative.");
                } else {
                    selectedTrip.setAvailableSeats(seats);
                    System.out.println("Available seats updated. Notifications sent to observers.");
                }
                break;

            case 2:
                updateTripPricingStrategy(selectedTrip);
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void updateTripPricingStrategy(Trip trip) {
        System.out.println("\n=== Update Pricing Strategy ===");
        System.out.println("Current price: $" + trip.calculatePrice());
        System.out.println("Select new pricing strategy:");
        System.out.println("1. Regular Season Pricing");
        System.out.println("2. Peak Season Pricing");
        System.out.println("3. Off Season Pricing");
        System.out.print("Enter choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                trip.setPricingStrategy(new RegularSeasonPricing());
                System.out.println("Regular Season Pricing applied. New price: $" + trip.calculatePrice());
                break;

            case 2:
                trip.setPricingStrategy(new PeakSeasonPricing());
                System.out.println("Peak Season Pricing applied. New price: $" + trip.calculatePrice());
                break;

            case 3:
                trip.setPricingStrategy(new OffSeasonPricing());
                System.out.println("Off Season Pricing applied. New price: $" + trip.calculatePrice());
                break;

            default:
                System.out.println("Invalid choice. Pricing strategy not changed.");
        }
    }

    private static void cancelTrip() {
        List<Trip> trips = tripService.getAllTrips();

        if (trips.isEmpty()) {
            System.out.println("No trips available to cancel.");
            return;
        }

        System.out.println("\n===== Cancel Trip =====");
        System.out.println("Select a trip to cancel:");

        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            System.out.println((i + 1) + ". " + trip.getDestination() + " from " + trip.getOrigin() +
                    " (" + trip.getDepartureDate() + " to " + trip.getArrivalDate() + ")");
        }

        System.out.print("Enter trip number (1-" + trips.size() + "): ");
        int tripIndex = getIntInput() - 1;

        if (tripIndex < 0 || tripIndex >= trips.size()) {
            System.out.println("Invalid trip selection.");
            return;
        }

        Trip selectedTrip = trips.get(tripIndex);
        System.out.print("Are you sure you want to cancel this trip? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            tripService.removeTrip(selectedTrip.getTripId());
            System.out.println("Trip cancelled successfully. All affected bookings have been notified.");
        } else {
            System.out.println("Trip cancellation aborted.");
        }
    }

    private static void viewAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n===== All Bookings =====");
        int index = 1;
        for (Booking booking : bookings) {
            System.out.println(index + ". " + booking);
            index++;
        }
    }

    private static void managePricingStrategies() {
        List<Trip> trips = tripService.getAllTrips();

        if (trips.isEmpty()) {
            System.out.println("No trips available to manage pricing.");
            return;
        }

        System.out.println("\n===== Manage Pricing Strategies =====");
        System.out.println("Select a trip to update pricing:");

        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            System.out.println((i + 1) + ". " + trip.getDestination() + " from " + trip.getOrigin() +
                    " (Current price: $" + trip.calculatePrice() + ")");
        }

        System.out.print("Enter trip number (1-" + trips.size() + "): ");
        int tripIndex = getIntInput() - 1;

        if (tripIndex < 0 || tripIndex >= trips.size()) {
            System.out.println("Invalid trip selection.");
            return;
        }

        updateTripPricingStrategy(trips.get(tripIndex));
    }

    // ===== PASSENGER METHODS =====

    private static void searchTrips() {
        System.out.println("\n===== Search Trips =====");

        System.out.print("Enter origin: ");
        String origin = scanner.nextLine();

        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String input = scanner.nextLine();
            try {
                date = LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        List<Trip> trips = tripService.searchTrips(origin, destination, date);

        if (trips.isEmpty()) {
            System.out.println("No trips found matching your criteria.");
        } else {
            System.out.println("\nFound " + trips.size() + " matching trips:");
            int index = 1;
            for (Trip trip : trips) {
                System.out.println(index + ". " + trip);

                // Show itinerary if available
                if (trip.getItinerary() != null) {
                    System.out.println("   Itinerary: " + trip.getItinerary().getDescription());
                }

                if (trip instanceof PredefinedPackage) {
                    PredefinedPackage pkg = (PredefinedPackage) trip;
                    System.out.println("   Package: " + pkg.getPackageName());
                    System.out.println("   Inclusions: " + String.join(", ", pkg.getInclusions()));
                }

                System.out.println("   Price: $" + trip.calculatePrice());
                System.out.println();
                index++;
            }
        }
    }

    private static void viewPredefinedPackages() {
        List<Trip> allTrips = tripService.getAllTrips();
        List<Trip> packages = new ArrayList<>();

        // Filter to only show predefined packages
        for (Trip trip : allTrips) {
            if (trip instanceof PredefinedPackage) {
                packages.add(trip);
            }
        }

        if (packages.isEmpty()) {
            System.out.println("No predefined packages available.");
            return;
        }

        System.out.println("\n===== Predefined Packages =====");
        int index = 1;
        for (Trip trip : packages) {
            PredefinedPackage pkg = (PredefinedPackage) trip;
            System.out.println(index + ". " + pkg.getPackageName());
            System.out.println("   From: " + trip.getOrigin() + " To: " + trip.getDestination());
            System.out.println("   Departure: " + trip.getDepartureDate() + " Return: " + trip.getArrivalDate());
            System.out.println("   Description: " + pkg.getDescription());
            System.out.println("   Inclusions: " + String.join(", ", pkg.getInclusions()));
            System.out.println("   Available Seats: " + trip.getAvailableSeats());
            System.out.println("   Price: $" + trip.calculatePrice());
            System.out.println();
            index++;
        }
    }

    private static void bookPredefinedPackage() {
        List<Trip> allTrips = tripService.getAllTrips();
        List<Trip> packages = new ArrayList<>();

        // Filter to only show predefined packages
        for (Trip trip : allTrips) {
            if (trip instanceof PredefinedPackage && trip.getAvailableSeats() > 0) {
                packages.add(trip);
            }
        }

        if (packages.isEmpty()) {
            System.out.println("No predefined packages available for booking.");
            return;
        }

        System.out.println("\n===== Book a Predefined Package =====");
        System.out.println("Available packages:");

        for (int i = 0; i < packages.size(); i++) {
            Trip trip = packages.get(i);
            PredefinedPackage pkg = (PredefinedPackage) trip;
            System.out.println((i + 1) + ". " + pkg.getPackageName() + " - $" + trip.calculatePrice() +
                    " - " + trip.getAvailableSeats() + " seats available");
        }

        System.out.print("Enter package number to book (1-" + packages.size() + "): ");
        int packageIndex = getIntInput() - 1;

        if (packageIndex < 0 || packageIndex >= packages.size()) {
            System.out.println("Invalid package selection.");
            return;
        }

        Trip selectedTrip = packages.get(packageIndex);

        // Book the trip
        Booking booking = bookingService.createBooking(currentUser, selectedTrip.getTripId());

        if (booking != null) {
            System.out.println("Booking successful! Your booking ID is: " + booking.getBookingId());
            System.out.println("Trip: " + booking.getTrip().getDestination() + " from " + booking.getTrip().getOrigin());
            System.out.println("Departure: " + booking.getTrip().getDepartureDate());
            System.out.println("Price: $" + booking.getTrip().calculatePrice());
        } else {
            System.out.println("Booking failed. Please try again.");
        }
    }

    private static void createCustomTrip() {
        if (!(currentUser instanceof Passenger)) {
            System.out.println("Only passengers can create custom trips.");
            return;
        }

        System.out.println("\n===== Create Custom Trip =====");

        // Start with the first city (origin)
        System.out.print("Enter starting city: ");
        String origin = scanner.nextLine();

        // This will store our complete itinerary
        TripComponent itinerary = new CityVisit(origin, 100);
        String currentCity = origin;
        String finalDestination = "";

        // Create itinerary with decorator pattern
        boolean addMoreCities = true;
        while (addMoreCities) {
            System.out.print("\nAdd next destination? (yes/no): ");
            String response = scanner.nextLine();

            if (!response.equalsIgnoreCase("yes")) {
                addMoreCities = false;
                continue;
            }

            System.out.print("Enter next city to visit: ");
            String nextCity = scanner.nextLine();
            finalDestination = nextCity;

            System.out.println("Select transportation from " + currentCity + " to " + nextCity + ":");
            System.out.println("1. Bus ($50)");
            System.out.println("2. Train ($75)");
            System.out.println("3. Flight ($200)");
            System.out.print("Enter choice: ");

            int transportChoice = getIntInput();

            // Add transportation decorator with fixed prices
            switch (transportChoice) {
                case 1:
                    itinerary = new BusTransport(itinerary, currentCity, nextCity, 50);
                    break;
                case 2:
                    itinerary = new TrainTransport(itinerary, currentCity, nextCity, 75);
                    break;
                case 3:
                    itinerary = new FlightTransport(itinerary, currentCity, nextCity, 200);
                    break;
                default:
                    System.out.println("Invalid choice, using Bus as default.");
                    itinerary = new BusTransport(itinerary, currentCity, nextCity, 50);
            }

            // Add services at this city
            itinerary = addServicesForCity(itinerary, nextCity);

            currentCity = nextCity;
        }

        if (finalDestination.isEmpty()) {
            System.out.println("You need to add at least one destination to create a trip.");
            return;
        }

        // Ask for basic trip details
        System.out.print("Enter number of passengers: ");
        int passengers = getIntInput();

        if (passengers <= 0) {
            System.out.println("Number of passengers must be positive.");
            return;
        }

        LocalDate departureDate = null;
        while (departureDate == null) {
            System.out.print("Enter departure date (YYYY-MM-DD): ");
            String input = scanner.nextLine();
            try {
                departureDate = LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        LocalDate arrivalDate = null;
        while (arrivalDate == null) {
            System.out.print("Enter return date (YYYY-MM-DD): ");
            String input = scanner.nextLine();
            try {
                arrivalDate = LocalDate.parse(input, dateFormatter);
                if (arrivalDate.isBefore(departureDate)) {
                    System.out.println("Return date cannot be before departure date.");
                    arrivalDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        Passenger creator = (Passenger) currentUser;
        Trip trip = tripService.createCustomTrip(finalDestination, origin, departureDate,
                arrivalDate, passengers, creator);

        // Add the itinerary to the trip
        trip.setItinerary(itinerary);

        // Add special requests
        boolean addMoreRequests = true;
        while (addMoreRequests) {
            System.out.print("Add a special request (or type 'done' to finish): ");
            String request = scanner.nextLine();

            if (request.equalsIgnoreCase("done")) {
                addMoreRequests = false;
            } else {
                ((CustomTrip) trip).addSpecialRequest(request);
                System.out.println("Special request added.");
            }
        }

        System.out.println("\nCustom Trip created successfully!");
        System.out.println("Trip ID: " + trip.getTripId());
        System.out.println("From: " + origin + " To: " + finalDestination);
        System.out.println("Itinerary: " + itinerary.getDescription());
        System.out.println("Total Cost: $" + itinerary.getCost());
    }

    private static TripComponent addServicesForCity(TripComponent itinerary, String city) {
        System.out.print("\nWould you like to add services in " + city + "? (yes/no): ");
        String response = scanner.nextLine();

        if (!response.equalsIgnoreCase("yes")) {
            return itinerary;
        }

        ServiceComponent service = new BaseService();
        boolean addMoreServices = true;

        while (addMoreServices) {
            System.out.println("\nAvailable services in " + city + ":");
            System.out.println("1. Hotel Stay ($100 per night)");
            System.out.println("2. Dining Service ($40 per meal)");
            System.out.println("3. Guide Service ($80 per day)");
            System.out.println("4. Finish adding services");
            System.out.print("Select a service to add: ");

            int serviceChoice = getIntInput();

            switch (serviceChoice) {
                case 1:
                    // Hotel Stay
                    System.out.print("Enter number of nights: ");
                    int nights = getIntInput();
                    service = new HotelStay(service, city + " Hotel", nights, 100);
                    System.out.println("Hotel stay added - $" + (nights * 100));
                    break;

                case 2:
                    // Dining Service
                    System.out.print("Enter number of meals: ");
                    int meals = getIntInput();
                    service = new DiningService(service, city + " Restaurant", meals, 40);
                    System.out.println("Dining service added - $" + (meals * 40));
                    break;

                case 3:
                    // Guide Service
                    System.out.print("Enter number of days: ");
                    int days = getIntInput();
                    service = new GuideService(service, city + " Guide", days, 80);
                    System.out.println("Guide service added - $" + (days * 80));
                    break;

                case 4:
                    addMoreServices = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        // Add the services to the itinerary cost and description
        if (!service.getDescription().equals("Basic service") || service.getCost() > 0) {
            System.out.println("\nServices added in " + city + ":");
            System.out.println("- " + service.getDescription());
            System.out.println("Total service cost: $" + service.getCost());

            // In a real application, we'd create a proper service decorator for the trip component
            // For simplicity, we'll just return the existing itinerary and consider the services
            // as part of the city visit
            double totalCost = itinerary.getCost() + service.getCost();
            String description = itinerary.getDescription() + " with services: " + service.getDescription();

            // This is a bit of a hack for the demo - in a real application, we'd have a proper
            // CityVisitWithServices decorator
            return new TripDecorator(itinerary) {
                @Override
                public String getDescription() {
                    return description;
                }

                @Override
                public double getCost() {
                    return totalCost;
                }
            };
        }

        return itinerary;
    }

    private static void viewMyBookings() {
        if (!(currentUser instanceof Passenger)) {
            System.out.println("Only passengers can view their bookings.");
            return;
        }

        List<Booking> bookings = bookingService.getBookingsByUser(currentUser.getUserId());

        if (bookings.isEmpty()) {
            System.out.println("You don't have any bookings.");
            return;
        }

        System.out.println("\n===== Your Bookings =====");
        int index = 1;
        for (Booking booking : bookings) {
            System.out.println(index + ". Booking ID: " + booking.getBookingId());
            System.out.println("   Trip: " + booking.getTrip().getDestination() + " from " + booking.getTrip().getOrigin());
            System.out.println("   Departure: " + booking.getTrip().getDepartureDate());
            System.out.println("   Status: " + booking.getStatus());
            System.out.println("   Price: $" + booking.getTrip().calculatePrice());
            System.out.println();
            index++;
        }
    }

    private static void viewMyCustomTrips() {
        if (!(currentUser instanceof Passenger)) {
            System.out.println("Only passengers can view their custom trips.");
            return;
        }

        Passenger passenger = (Passenger) currentUser;
        List<Trip> myCustomTrips = tripService.getCustomTripsByPassenger(passenger);

        if (myCustomTrips.isEmpty()) {
            System.out.println("You haven't created any custom trips.");
            return;
        }

        System.out.println("\n===== Your Custom Trips =====");
        int index = 1;
        for (Trip trip : myCustomTrips) {
            CustomTrip customTrip = (CustomTrip) trip;
            System.out.println(index + ". From: " + trip.getOrigin() + " To: " + trip.getDestination());
            System.out.println("   Departure: " + trip.getDepartureDate() + " Return: " + trip.getArrivalDate());

            if (trip.getItinerary() != null) {
                System.out.println("   Itinerary: " + trip.getItinerary().getDescription());
                System.out.println("   Total Cost: $" + trip.getItinerary().getCost());
            }

            System.out.println("   Special Requests: " + String.join(", ", customTrip.getSpecialRequests()));
            System.out.println();
            index++;
        }
    }

    private static void cancelBooking() {
        if (!(currentUser instanceof Passenger)) {
            System.out.println("Only passengers can cancel their bookings.");
            return;
        }

        List<Booking> bookings = bookingService.getBookingsByUser(currentUser.getUserId());

        if (bookings.isEmpty()) {
            System.out.println("You don't have any bookings to cancel.");
            return;
        }

        System.out.println("\n===== Cancel a Booking =====");
        System.out.println("Your bookings:");

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            if (booking.getStatus() != BookingStatus.CANCELLED) {
                System.out.println((i + 1) + ". " + booking.getTrip().getDestination() +
                        " (Departure: " + booking.getTrip().getDepartureDate() + ")");
            } else {
                System.out.println((i + 1) + ". " + booking.getTrip().getDestination() +
                        " - ALREADY CANCELLED");
            }
        }

        System.out.print("Enter booking number to cancel (1-" + bookings.size() + "): ");
        int bookingIndex = getIntInput() - 1;

        if (bookingIndex < 0 || bookingIndex >= bookings.size()) {
            System.out.println("Invalid booking selection.");
            return;
        }

        Booking selectedBooking = bookings.get(bookingIndex);

        if (selectedBooking.getStatus() == BookingStatus.CANCELLED) {
            System.out.println("This booking is already cancelled.");
            return;
        }

        System.out.print("Are you sure you want to cancel this booking? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            boolean cancelled = bookingService.cancelBooking(selectedBooking.getBookingId());

            if (cancelled) {
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("Failed to cancel booking. Please try again.");
            }
        } else {
            System.out.println("Booking cancellation aborted.");
        }
    }

    // ===== UTILITY METHODS =====

    private static int getIntInput() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}