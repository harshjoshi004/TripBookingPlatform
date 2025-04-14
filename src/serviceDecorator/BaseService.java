package serviceDecorator;

public class BaseService extends ServiceComponent {
    @Override
    public String getDescription() {
        return "Basic service";
    }

    @Override
    public double getCost() {
        return 0.0;
    }
}
