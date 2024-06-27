package demo.lowcode.device.coffeemaker.service;

import demo.lowcode.common.extend.device.DeviceService;

public class CoffeeMakerService extends DeviceService {
    public void start(){
        throw new RuntimeException("NoSupportedOperation");
    }

    public void makeCoffee(){
        throw new RuntimeException("NoSupportedOperation");
    }

    public void check(Integer code){
        throw new RuntimeException("NoSupportedOperation");
    }
}
