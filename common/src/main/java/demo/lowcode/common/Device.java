package demo.lowcode.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Device implements Action {
    protected List<Service> services = new ArrayList<>();
    protected List<String> properties = new ArrayList<>();
    protected List<String> operations = new ArrayList<>();
    protected List<String> events = new ArrayList<>();

    public void bindService(Service service) {
        services.add(service);
    }

    public void addProperties(String property) {
        properties.add(property);
    }

    public void addOperation(String operation) {
        operations.add(operation);
    }

    public void addEvent(String event) {
        events.add(event);
    }

    public abstract int invokeOperation(String operation);
    public abstract void addEventListener(String eventName, EventListener eventListener);
}
