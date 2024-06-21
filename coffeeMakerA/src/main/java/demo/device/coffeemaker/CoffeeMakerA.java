package demo.device.coffeemaker;

import demo.device.coffeemaker.listener.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CoffeeMakerA {
    private CoffeeMakerData coffeeMakerData;

    private Map<String, List<EventListener>> beforeEventListeners = new HashMap<>();
    private Map<String, List<EventListener>> afterEventListeners = new HashMap<>();

    public CoffeeMakerA(){
        this.coffeeMakerData = new CoffeeMakerData("CoffeeMaker1");
    }

    /** 获取属性 */
    public CoffeeMakerData getCoffeeMakerData() {
        return coffeeMakerData;
    }

    /** 注册事件 */
    public void addBeforeEventListener(String operationType, EventListener listener) {
        beforeEventListeners.computeIfAbsent(operationType, k -> new ArrayList<>()).add(listener);
    }

    public void addAfterEventListener(String operationType, EventListener listener) {
        afterEventListeners.computeIfAbsent(operationType, k -> new ArrayList<>()).add(listener);
    }

    private void triggerBeforeEvent(String operationType, CoffeeMakerEvent event) {
        List<EventListener> listeners = beforeEventListeners.get(operationType);
        if (listeners != null) {
            for (EventListener listener : listeners) {
                listener.handleEvent(event);
            }
        }
    }

    private void triggerAfterEvent(String operationType, CoffeeMakerEvent event) {
        List<EventListener> listeners = afterEventListeners.get(operationType);
        if (listeners != null) {
            for (EventListener listener : listeners) {
                listener.handleEvent(event);
            }
        }
    }

    /** 命令 */
    public void on() {
        System.out.println("starting...");
    }

    public void standBy() {
        System.out.println("StandBy");
    }

    public void makeCoffee() {
        CoffeeMakerEvent beforeEvent = new CoffeeMakerEvent("Preparing...");
        triggerBeforeEvent("makeCoffee", beforeEvent);

        System.out.println("Making coffee...");

        CoffeeMakerEvent afterEvent = new CoffeeMakerEvent("Coffee is ready");
        triggerAfterEvent("makeCoffee", afterEvent);
    }

    public void sleep(){
        System.out.println("Sleeping...");
    }
}
