package serviceDecorator;

public class DiningService extends ServiceDecorator {
    private String restaurantName;
    private int meals;
    private double costPerMeal;

    public DiningService(ServiceComponent component, String restaurantName, int meals, double costPerMeal) {
        super(component);
        this.restaurantName = restaurantName;
        this.meals = meals;
        this.costPerMeal = costPerMeal;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", " + meals + " meals at " + restaurantName;
    }

    @Override
    public double getCost() {
        return super.getCost() + (meals * costPerMeal);
    }
}
