package lowcode.device.coffeemaker.generate.service;

import demo.lowcode.common.*;
import lowcode.device.coffeemaker.service.*;
import java.util.*;

public class BService extends CoffeeMakerService {

    private String uri = "";
    private List<String> coffeeType = List.of("摩卡", "美式");
    private List<Double> sugar = List.of(0.3, 0.5, 0.8, 1.0);

    public BService(String uri){
        this.uri = uri;
    }

    @Override
    public Map<String, Object> getProperty() {
        Map<String, Object> result = new HashMap<>();
        result.put( {{ coffeeType[] }} );
        return result;
    }

    @Override
    public void start() {
        System.out.println("Starting Coffee Maker BService ...");
        try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println( "start" + {{code}} ); 
    }

    @Override
    public void makeCoffee(String coffee_type) {
        System.out.println("Starting making coffee (Accessing URI: " + uri + ")" );
        try {Thread.sleep(8000);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("Done making coffee.");
    }

    @Override
    public void check() {
        System.out.println("Finishing" + code );
        try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("Check finished");
    }

}