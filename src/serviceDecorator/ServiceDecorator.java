package serviceDecorator;

public abstract class ServiceDecorator extends ServiceComponent {
    protected ServiceComponent component;

    public ServiceDecorator(ServiceComponent component) {
        this.component = component;
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public double getCost() {
        return component.getCost();
    }
}