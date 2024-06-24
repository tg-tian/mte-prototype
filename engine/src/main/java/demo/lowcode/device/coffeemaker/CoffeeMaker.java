package demo.lowcode.device.coffeemaker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.extend.device.Device;
import demo.lowcode.common.extend.device.DeviceService;
import demo.lowcode.common.EventListener;
import demo.lowcode.device.coffeemaker.event.CoffeeMakerEvent;
import demo.lowcode.device.coffeemaker.service.CoffeeMakerService;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CoffeeMaker extends Device {
    private Map<String, List<EventListener>> beforeEventListeners = new HashMap<>();
    private Map<String, List<EventListener>> afterEventListeners = new HashMap<>();
    private Map<String, List<EventListener>> errorEventListeners = new HashMap<>();

    @Value("${definitionPath}")
    private String definitionPath;

    public CoffeeMaker() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("application.properties");
            Properties properties = new Properties();
            properties.load(input);
            definitionPath = properties.getProperty("definitionPath");
            File file = new File(definitionPath+"CoffeeMaker.json");
            JsonNode rootNode = objectMapper.readTree(file);

            // 读取commands数组
            JsonNode commandsNode = rootNode.path("commands");
            List<String> commands = new ArrayList<>();
            if (commandsNode.isArray()) {
                for (JsonNode command : commandsNode) {
                    commands.add(command.path("name").asText());
                }
            }
            setOperations(commands);

            // 读取events数组
            JsonNode eventsNode = rootNode.path("events");
            List<String> events = new ArrayList<>();
            if (eventsNode.isArray()) {
                for (JsonNode event : eventsNode) {
                    events.add(event.asText());
                }
            }
            setEvents(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onStart(String operationType) {
        List<EventListener> listeners = beforeEventListeners.get(operationType);
        if (listeners != null) {
            for (EventListener listener: listeners) {
                listener.handleEvent();
            }
        }
    }

    private void onComplete(String operationType) {
        List<EventListener> listeners = afterEventListeners.get(operationType);
        if (listeners != null) {
            for (EventListener listener : listeners) {
                listener.handleEvent();
            }
        }
    }

    private void onError(String operationType, CoffeeMakerEvent event) {
        List<EventListener> listeners = errorEventListeners.get(operationType);
        if (listeners != null) {
            for (EventListener listener : listeners) {
                listener.handleEvent(event);
            }
        }
    }

    @Override
    public void addEventListener(String eventName, EventListener eventHandler) {
        if (eventName.startsWith("on") && eventName.endsWith("Error")) {
            String operation = eventName.substring(2, eventName.length() - 5);// 提取中间的操作名称部分
            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);// 将首字母小写化
            errorEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
        }else if (eventName.startsWith("on") && eventName.endsWith("Complete")){
            String operation = eventName.substring(2, eventName.length() - 8);// 提取中间的操作名称部分
            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);// 将首字母小写化
            afterEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
        }else if (eventName.startsWith("on") && eventName.endsWith("Start")){
            String operation = eventName.substring(2, eventName.length() - 5);// 提取中间的操作名称部分
            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);// 将首字母小写化
            beforeEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
        }
    }

    @Override
    public int invokeOperation(String operation) {
        try {
            onStart(operation);// 操作前事件
            boolean flag = false; // 判断是否有符合的设备
            for (DeviceService deviceService : deviceServices) {
                if (deviceService instanceof CoffeeMakerService) {
                    try {
                        Method method = CoffeeMakerService.class.getDeclaredMethod(operation);
                        method.invoke(deviceService);
                        flag = true;
                        break;
                    } catch (InvocationTargetException e) {
                        // ignore
                    }
                }
            }
            if (!flag){
                throw new RuntimeException("There is no available service to handle operation "+operation);
            }
            onComplete(operation);// 操作结束事件
            return 0;
        } catch (Exception e) {
            onError(operation, new CoffeeMakerEvent(e.getMessage(), 400));// 操作异常事件
            return 1;
        }
    }

    @Override
    public int execute(Object... args) {
        if (args.length == 1 && args[0] instanceof String){
            return invokeOperation((String) args[0]);
        }
        return 2;
    }
}
