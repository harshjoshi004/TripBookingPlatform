package tripDecorator;

public class CityVisit extends TripComponent {
    private String cityName;
    private double cost;

    public CityVisit(String cityName, double cost) {
        this.cityName = cityName;
        this.cost = cost;
    }

    @Override
    public String getDescription() {
        return "Visit to " + cityName;
    }

    @Override
    public double getCost() {
        return cost;
    }
}
