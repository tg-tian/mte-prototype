package lowcode.device.coffeemaker.event;

import demo.lowcode.common.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class CoffeeMakerEvent extends Event {
    private Object[] information;
    public CoffeeMakerEvent(String message, int code) {
        setMessage(message);
        setCode(code);
    }
    public CoffeeMakerEvent(String message, int code, Object... args) {
        setMessage(message);
        setCode(code);
        this.information = args;
    }
}