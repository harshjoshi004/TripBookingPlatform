package serviceDecorator;

public class HotelStay extends ServiceDecorator {
    private String hotelName;
    private int nights;
    private double costPerNight;

    public HotelStay(ServiceComponent component, String hotelName, int nights, double costPerNight) {
        super(component);
        this.hotelName = hotelName;
        this.nights = nights;
        this.costPerNight = costPerNight;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", " + nights + " nights at " + hotelName;
    }

    @Override
    public double getCost() {
        return super.getCost() + (nights * costPerNight);
    }
}
