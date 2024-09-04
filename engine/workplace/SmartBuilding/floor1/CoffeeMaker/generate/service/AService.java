package lowcode.device.coffeemaker.generate.service;

import demo.lowcode.common.*;
import lowcode.device.coffeemaker.service.*;
import java.util.*;

public class AService extends CoffeeMakerService {

    private String uri = "";
    private List<String> coffeeType = List.of("摩卡", "美式");
    private List<Double> sugar = List.of(0.3, 0.5, 0.8, 1.0);

    public AService(String uri){
        this.uri = uri;
    }

    @Override
    public Map<String, Object> getProperty() {
        Map<String, Object> result = new HashMap<>();
        result.put("coffeeType",coffeeType);
        return result;
    }

    @Override
    public void start() {
        System.out.println("Accessing URI: " + uri + "on");
    }

    @Override
    public void makeCoffee(String coffeeType) {
        System.out.println("Accessing URI: " + uri + "makeCoffee");
        System.out.println(coffeeType);
    }

}