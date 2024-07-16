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
        System.out.println("Starting Coffee Maker BService ...");
        try {
            // 让当前线程休眠8秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // 捕获并处理中断异常
            e.printStackTrace();
        }
        System.out.println("Started (B).");
    }
    @Override
    public void check(Integer param0) {
        System.out.println("Finishing (B)");
        try {
            // 让当前线程休眠8秒
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // 捕获并处理中断异常
            e.printStackTrace();
        }
        System.out.println("Check finished");
    }
    @Override
    public void makeCoffee(LinkedHashMap executeArgs) {
        System.out.println("Starting making coffee (Accessing URI: " + uri + " (B)");
        try {
            // 让当前线程休眠8秒
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            // 捕获并处理中断异常
            e.printStackTrace();
        }
        System.out.println("Done making coffee.");
    }
}
