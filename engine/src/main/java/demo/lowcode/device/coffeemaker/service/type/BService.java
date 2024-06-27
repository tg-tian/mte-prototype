package demo.lowcode.device.coffeemaker.service.type;
import demo.lowcode.device.coffeemaker.service.CoffeeMakerService;
public class BService extends CoffeeMakerService {
    private String uri;
    public BService(String uri) { this.uri = uri; }
    @Override
    public void start() {
        System.out.println("Accessing URI: " + uri + "start");
    }
    @Override
    public void check(Integer param0) {
        System.out.println("Accessing URI: " + uri + "check");
        System.out.println("Param0: " + param0);
    }
    @Override
    public void makeCoffee() {
        System.out.println("Accessing URI: " + uri + "makeCoffee");
    }
}
