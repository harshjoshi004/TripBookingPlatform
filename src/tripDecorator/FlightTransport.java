package tripDecorator;

public class FlightTransport extends TripDecorator {
    private String fromCity;
    private String toCity;
    private double cost;

    public FlightTransport(TripComponent component, String fromCity, String toCity, double cost) {
        super(component);
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.cost = cost;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Flight from " + fromCity + " to " + toCity;
    }

    @Override
    public double getCost() {
        return super.getCost() + cost;
    }
}
