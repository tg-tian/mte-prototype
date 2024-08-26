package workplace.CoffeeMaker.Command

import demo.lowcode.device.coffeemaker.event.CoffeeMakerEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class MakeCoffeeController {
    public void {{onMakeCoffeeStartSignature}} {
        {{onMakeCoffeeStartBody}}
    }
    public void {{onMakeCoffeeCompleteSignature}} {
        {{onMakeCoffeeCompletBody}}
    }
    public void {{onMakeCoffeeErrorSignature}} {
        {{onMakeCoffeeErrorBody}}
    }

}



/**
 * //其中的CoffeeMakerEvent应该在发布的时候打包好
 *实际生成
 *public class MakeCoffeeController {
 *     public void prepare() {
 *         System.out.println("Start the CoffeeMaker.Sending message...");
 *     }
 *
 *     public void sendMessage(Event event) {
 *         System.out.println("Coffee is ready. Sending message...");
 *         System.out.println("Your coffee is ready. coffee information: "+Arrays.toString((CoffeeMakerEvent)event.getInformation()));
 *     }
 *     public void errorAlert(Event event) {
 *         System.out.println("Error:  " + (CoffeeMakerEvent)event.getMessage());
 *     }
 * }
 *
 * */