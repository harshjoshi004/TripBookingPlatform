package tripDecorator;

public class BusTransport extends TripDecorator {
    private String fromCity;
    private String toCity;
    private double cost;

    public BusTransport(TripComponent component, String fromCity, String toCity, double cost) {
        super(component);
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.cost = cost;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Bus from " + fromCity + " to " + toCity;
    }

    @Override
    public double getCost() {
        return super.getCost() + cost;
    }
}
