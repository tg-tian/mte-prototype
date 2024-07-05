package demo.lowcode.device.coffeemaker.service.type;
import demo.lowcode.device.coffeemaker.service.CoffeeMakerService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BService extends CoffeeMakerService {
    private String uri;
    public BService(String uri) { this.uri = uri; }

    @Override
    public Map<String, Object> getProperty() {
        Map<String, Object> result = new HashMap<>();
        result.put("coffeeType", Arrays.asList());
        return result;
    }

    @Override
    public void start() {
        System.out.println("Accessing URI: " + uri + "start");
    }
    @Override
    public void check(Integer param0) {
        System.out.println("Accessing URI: " + uri + "check");
//        System.out.println("Param0: " + param0);
    }
    @Override
    public void makeCoffee(LinkedHashMap executeArgs) {
        System.out.println("Accessing URI: " + uri + "makeCoffee");
    }
}
