package demo.lowcode.device.coffeemaker.service;

import demo.lowcode.common.extend.device.DeviceService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


// 主要是调用事件和参数
public class CoffeeMakerService extends DeviceService {
    @Override
    public Map<String, Object> getProperty() {
        return new HashMap<>();
    }

    public void start(){
        throw new RuntimeException("NoSupportedOperation");
    }

    public void makeCoffee(LinkedHashMap executeArgs){
        throw new RuntimeException("NoSupportedOperation");
    }

    public void check(){
        throw new RuntimeException("NoSupportedOperation");
    }
}
