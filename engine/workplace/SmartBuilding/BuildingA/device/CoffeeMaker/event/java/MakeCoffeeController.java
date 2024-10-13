package lowcode.device.coffeemaker.generate.event;

import lombok.*;
import java.util.*;
import demo.lowcode.common.*;
import lowcode.device.coffeemaker.event.*;

@AllArgsConstructor
public class MakeCoffeeController {

    public void prepare() {
//        System.out.println("Start the CoffeeMaker.Sending message...");
        System.out.println("Before making coffee, preparing...");
    }

    public void sendMessage(Event event) {
        System.out.println("Coffee is ready. Sending message...");
        System.out.println("Your coffee is ready. coffee information: "+Arrays.toString(((CoffeeMakerEvent)event).getInformation()));
    }

    public void errorAlert(Event event) {
        System.out.println("Error:  " + event.getMessage());
    }

}
