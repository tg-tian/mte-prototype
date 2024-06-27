package demo.lowcode.device.coffeemaker.service.type;
import demo.lowcode.device.coffeemaker.service.CoffeeMakerService;
public class AService extends CoffeeMakerService {
    private String uri;
    public AService(String uri) { this.uri = uri; }
    @Override
    public void start() {
        System.out.println("Accessing URI: " + uri + "on");
    }
    @Override
    public void makeCoffee() {
        System.out.println("Accessing URI: " + uri + "makeCoffee");
    }
}
