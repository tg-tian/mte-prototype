package demo.lowcode.device.coffeemaker;

import demo.lowcode.common.Device;
import demo.lowcode.common.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoffeeMakerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void coffeeMakerInit() {
        try {
            Class<?> deviceClass = Class.forName("demo.lowcode.device.coffeemaker.CoffeeMaker");
            Device device = (Device) deviceClass.getConstructor().newInstance();

            Class<?> AServiceClass = Class.forName("demo.lowcode.device.coffeemaker.service.type.AService");
            device.bindService((Service) AServiceClass.getConstructor().newInstance());

            device.execute("makeCoffee");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }
}
