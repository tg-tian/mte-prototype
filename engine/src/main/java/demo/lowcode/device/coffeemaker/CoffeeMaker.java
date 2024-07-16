package demo.lowcode.device.coffeemaker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.ActionExecResult;
import demo.lowcode.common.extend.device.Device;
import demo.lowcode.common.extend.device.DeviceService;
import demo.lowcode.common.EventListener;
import demo.lowcode.device.coffeemaker.event.CoffeeMakerEvent;
import demo.lowcode.device.coffeemaker.service.CoffeeMakerService;
import demo.lowcode.engine.business.MockBusiness;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
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
    public int invokeOperation(String operation, Object... args) {
        try {
            onStart(operation);// 操作前事件
            boolean flag = false; // 判断是否有符合的设备
            if (deviceService instanceof CoffeeMakerService) {
                try {
                    Method method;
                    if (args.length > 0) {
                        // 获取参数类型数组
                        Class<?>[] parameterTypes = new Class<?>[args.length];
                        for (int i = 0; i < args.length; i++) {
                            parameterTypes[i] = args[i].getClass();
                        }
                        method = CoffeeMakerService.class.getDeclaredMethod(operation, parameterTypes);
                        method.invoke(deviceService, args);
                    } else {
                        // 找到无参数的方法
                        method = CoffeeMakerService.class.getDeclaredMethod(operation);
                        method.invoke(deviceService);
                    }

                    flag = true;
                } catch (InvocationTargetException e) {
                    // ignore
                }
            }
            if (!flag){
                throw new RuntimeException("There is no available service to handle operation "+operation);
            }
            onComplete(operation);// 操作结束事件
            return 0;
        } catch (Exception e) {
            onError(operation, new CoffeeMakerEvent(e.getMessage(), 400));// 操作异常事件
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public ActionExecResult execute(Object... args) {// 第一个是调用的方法名；第二个参数是父节点的输出，类型为int/Map；第三个参数是用户输入的参数值Map
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
