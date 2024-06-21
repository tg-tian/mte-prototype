package demo.device.coffeemaker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class CoffeeMakerData {
    private String name;
    private String status;
    private int water;
    private int coffee;
    private int cup;
    // ...其他属性

    public CoffeeMakerData(String name) {
        this.name = name;
        this.status = "Ready";
    }
}
