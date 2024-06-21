package demo.lowcode.device.coffeemaker.service;

import demo.lowcode.common.Service;

public abstract class CoffeeMakerService extends Service {
    public abstract void start();
    public abstract void makeCoffee();
    public abstract void check();
}
