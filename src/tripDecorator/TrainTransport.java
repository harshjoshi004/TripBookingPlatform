package tripDecorator;

public class TrainTransport extends TripDecorator {
    private String fromCity;
    private String toCity;
    private double cost;

    public TrainTransport(TripComponent component, String fromCity, String toCity, double cost) {
        super(component);
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.cost = cost;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Train from " + fromCity + " to " + toCity;
    }

    @Override
    public double getCost() {
        return super.getCost() + cost;
    }
}
