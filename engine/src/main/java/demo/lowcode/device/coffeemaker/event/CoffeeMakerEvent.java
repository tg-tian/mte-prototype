package demo.lowcode.device.coffeemaker.event;

import demo.lowcode.common.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CoffeeMakerEvent extends Event {
    public CoffeeMakerEvent(String message, int code) {
        setMessage(message);
        setCode(code);
    }
}
