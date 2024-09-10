package lowcode.device.coffeemaker.service;

import demo.lowcode.common.*;
import demo.lowcode.common.device.DeviceService;
import java.util.*;

public class CoffeeMakerService extends DeviceService {
    @Override
    public Map<String, Object> getProperty() {
        return new HashMap<>();
    }

    public void start() {
        throw new RuntimeException("NoSupportedOperation");
    }

    public void makeCoffee(String coffeeType) {
        throw new RuntimeException("NoSupportedOperation");
    }

    public void check() {
        throw new RuntimeException("NoSupportedOperation");
    }

}