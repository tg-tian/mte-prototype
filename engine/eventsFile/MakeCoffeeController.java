package demo.lowcode.platform.eventhandler.classes;

import demo.lowcode.device.coffeemaker.event.CoffeeMakerEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class MakeCoffeeController {
    public void prepare() {
        System.out.println("Before making coffee...");
    }

    public void sendMessage(CoffeeMakerEvent event) {
        System.out.println("Coffee is ready. Sending message...");
        System.out.println("Your coffee is ready. coffee information: "+Arrays.toString(event.getInformation()));
    }

    public void errorAlert(CoffeeMakerEvent event) {
        System.out.println("Error:  " + event.getMessage());
    }
}
