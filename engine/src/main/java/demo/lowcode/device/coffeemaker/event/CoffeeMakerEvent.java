package demo.lowcode.device.coffeemaker.event;

import demo.lowcode.common.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data

/**
 * 设备事件类，用于调用实现不同事件的功能，暂定编写在创建阶段，对于应用开发人员来说仅做使用
 *
 * */

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
