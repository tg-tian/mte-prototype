package lowcode.device.coffeemaker;

import demo.lowcode.common.*;
import demo.lowcode.common.EventListener;
import demo.lowcode.common.device.*;
import lowcode.device.coffeemaker.event.*;

import lowcode.device.coffeemaker.service.*;

import org.springframework.stereotype.Component;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

@Component
public class CoffeeMaker extends Device{
    private Map<String, List<EventListener>> beforeEventListeners = new HashMap<>();
    private Map<String, List<EventListener>> afterEventListeners = new HashMap<>();
    private Map<String, List<EventListener>> errorEventListeners = new HashMap<>();
    private String definitionPath = "D:/projects/ubml/mte-prototype/engine/workplace/SmartBuilding/floor1/CoffeeMaker/";
    public CoffeeMaker() {
        setOperations(Arrays.asList("Start", "MakeCoffee", "Check"));
        setEvents(Arrays.asList("onMakeCoffeeStart", "onMakeCoffeeComplete", "onMakeCoffeeError"));
    }
    private void onStart(String operationType, CoffeeMakerEvent event) {
        List<EventListener> listeners = beforeEventListeners.get(operationType);
        if (listeners != null) {
            for (EventListener listener: listeners) {
                if (event != null){
                    listener.handleEvent(event);
                }else {
                    listener.handleEvent();
                }
            }
        }
    }

    private void onComplete(String operationType, CoffeeMakerEvent event) {
        List<EventListener> listeners = afterEventListeners.get(operationType);
        if (listeners != null) {
            for (EventListener listener: listeners) {
                if (event != null){
                    listener.handleEvent(event);
                }else {
                    listener.handleEvent();
                }
            }
        }
    }

    private void onError(String operationType, CoffeeMakerEvent event) {
        List<EventListener> listeners = errorEventListeners.get(operationType);
        if (listeners != null) {
            for (EventListener listener: listeners) {
                if (event != null){
                    listener.handleEvent(event);
                }else {
                    listener.handleEvent();
                }
            }
        }
    }

    @Override
    public void addEventListener(String eventName, EventListener eventHandler) {
        if (eventName.startsWith("on") && eventName.endsWith("Error")) {
            String operation = eventName.substring(2, eventName.length() - 5);
            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);
            errorEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
        }else if (eventName.startsWith("on") && eventName.endsWith("Complete")){
            String operation = eventName.substring(2, eventName.length() - 8);
            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);
            afterEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
        }else if (eventName.startsWith("on") && eventName.endsWith("Start")){
            String operation = eventName.substring(2, eventName.length() - 5);
            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);
            beforeEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
        }
    }

@Override
public int invokeOperation(String operation, Object... args) {
    try {
        onStart(operation, null);
        boolean flag = false;
        if (deviceService instanceof CoffeeMakerService) {
                try {
                    Method method;
                    if (args.length > 0) {
                        Class<?>[] parameterTypes = new Class<?>[args.length];
                        for (int i = 0; i < args.length; i++) {
                            parameterTypes[i] = args[i].getClass();
                        }
                        method = CoffeeMakerService.class.getDeclaredMethod(operation, parameterTypes);
                        method.invoke(deviceService, args);
                        onComplete(operation, new CoffeeMakerEvent(operation, 200, args));
                    } else {
                        method = CoffeeMakerService.class.getDeclaredMethod(operation);
                        method.invoke(deviceService);
                        onComplete(operation, null);
                    }
                    flag = true;
                } catch (InvocationTargetException e) {
                    // ignore
                }
            }
            if (!flag){
                throw new RuntimeException("There is no available service to handle operation "+operation);
            }
            return 0;
        } catch (Exception e) {
            onError(operation, new CoffeeMakerEvent(e.getMessage(), 400));
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public ActionExecResult execute(Object... args) {
        ActionExecResult actionExecResult = new ActionExecResult();
        if (args.length >= 1 && args[0] instanceof String) {
            String operationName = (String) args[0];
            Object[] operationArgs = Arrays.copyOfRange(args, 1, args.length);
            int code = invokeOperation(operationName, operationArgs);
            actionExecResult.setCode(code);
        } else {
            actionExecResult.setCode(2);
        }
        return actionExecResult;
    }

}