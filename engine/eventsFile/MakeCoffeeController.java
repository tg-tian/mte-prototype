package demo.lowcode.platform.eventhandler.classes;

import demo.lowcode.device.coffeemaker.event.CoffeeMakerEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MakeCoffeeController {
    public void prepare() {
        System.out.println("Before making coffee...");
    }

    public void sendMessage() {
        System.out.println("发送短信...");
    }

    public void errorAlert(CoffeeMakerEvent event) {
        System.out.println("Error:  " + event.getMessage());
    }
}
