package serviceDecorator;

public class GuideService extends ServiceDecorator {
    private String guideName;
    private int days;
    private double costPerDay;

    public GuideService(ServiceComponent component, String guideName, int days, double costPerDay) {
        super(component);
        this.guideName = guideName;
        this.days = days;
        this.costPerDay = costPerDay;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", " + days + " days with guide " + guideName;
    }

    @Override
    public double getCost() {
        return super.getCost() + (days * costPerDay);
    }
}
