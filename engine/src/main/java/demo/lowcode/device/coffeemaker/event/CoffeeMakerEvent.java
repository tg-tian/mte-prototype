package demo.lowcode.device.coffeemaker.event;

import demo.lowcode.common.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
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
