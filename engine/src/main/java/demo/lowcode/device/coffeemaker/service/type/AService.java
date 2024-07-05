package demo.lowcode.device.coffeemaker.service.type;
import demo.lowcode.device.coffeemaker.service.CoffeeMakerService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AService extends CoffeeMakerService {
    private String uri;
    public AService(String uri) { this.uri = uri; }

    @Override
    public Map<String, Object> getProperty() {
        Map<String, Object> result = new HashMap<>();
        result.put("coffeeType", Arrays.asList("摩卡", "美式"));
        return result;
    }

    @Override
    public void start() {
        System.out.println("Accessing URI: " + uri + "on");
    }
    @Override
    public void makeCoffee(LinkedHashMap executeArgs) {
        System.out.println("Accessing URI: " + uri + "makeCoffee");
        System.out.println(executeArgs);
    }
}
