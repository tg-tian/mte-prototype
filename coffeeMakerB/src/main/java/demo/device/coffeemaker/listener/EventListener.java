package demo.device.coffeemaker.listener;

import demo.device.coffeemaker.CoffeeMakerEvent;
import org.springframework.stereotype.Component;

@Component
public interface EventListener {
    void handleEvent(CoffeeMakerEvent event);
}
