package demo.lowcode.common.extend.device;

import demo.lowcode.common.Action;
import demo.lowcode.common.EventListener;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Device implements Action {
    protected List<DeviceService> deviceServices = new ArrayList<>();
    protected List<String> properties = new ArrayList<>();
    protected List<String> operations = new ArrayList<>();
    protected List<String> events = new ArrayList<>();

    public void bindService(DeviceService deviceService) {
        deviceServices.add(deviceService);
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

    public abstract int invokeOperation(String operation, Object... args);
    public abstract void addEventListener(String eventName, EventListener eventListener);
}
