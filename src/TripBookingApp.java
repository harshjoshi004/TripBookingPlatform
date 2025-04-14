public class TripBookingApp {
    private static TripBookingApp instance;

    // Private constructor to prevent direct instantiation
    private TripBookingApp() {
        System.out.println("TripBookingApp instance created");
    }

    // Static method to get the singleton instance
    public static TripBookingApp getInstance() {
        if (instance == null) {
            instance = new TripBookingApp();
        }
        return instance;
    }
}