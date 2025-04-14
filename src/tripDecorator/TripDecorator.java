package tripDecorator;

public abstract class TripDecorator extends TripComponent {
    protected TripComponent component;

    public TripDecorator(TripComponent component) {
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
