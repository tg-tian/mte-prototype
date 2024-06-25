package demo.lowcode.device.coffeemaker.service;

import demo.lowcode.common.extend.device.DeviceService;

public abstract class CoffeeMakerService extends DeviceService {
    public abstract void start();
    public abstract void makeCoffee();
    public abstract void check(Object params);
}
