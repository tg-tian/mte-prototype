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
    //用于存储不同的事件监听器
    private Map<String, List<EventListener>> beforeEventListeners = new HashMap<>();
    private Map<String, List<EventListener>> afterEventListeners = new HashMap<>();
    private Map<String, List<EventListener>> errorEventListeners = new HashMap<>();
    @Value("${definitionPath}")
    private String definitionPath;
    //构造函数，用于读取相应的json文件
    public CoffeeMaker() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("application.properties");
            Properties properties = new Properties();
            properties.load(input);
            definitionPath = properties.getProperty("definitionPath");
            File file = new File(definitionPath+"CoffeeMaker/CoffeeMaker.json");
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
            for (EventListener listener : listeners) {
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
            for (EventListener listener : listeners) {
                if (event != null){
                    listener.handleEvent(event);
                }else {
                    listener.handleEvent();
                }
            }
        }
    }
    //事件监听器的注册
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
    //操作执行流程
    @Override
    public int invokeOperation(String operation, Object... args) {
        try {
            onStart(operation, null);// 操作前事件
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

                        onComplete(operation, new CoffeeMakerEvent(operation, 200, args));// 操作结束事件
                    } else {
                        // 找到无参数的方法
                        method = CoffeeMakerService.class.getDeclaredMethod(operation);
                        method.invoke(deviceService);

                        onComplete(operation, null);// 操作结束事件
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
            onError(operation, new CoffeeMakerEvent(e.getMessage(), 400));// 操作异常事件
            e.printStackTrace();
            return 1;
        }
    }
    //设备操作执行
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

/**
 * @Component: 表明这个类是一个 Spring 组件，Spring 框架会自动扫描并将其实例化为一个 bean。
 * @Value("${definitionPath}"): 从配置文件中读取 definitionPath 的值，这个值用于加载咖啡机的 JSON 配置文件。
 * @Resource 和 @Autowired: 这两者用于自动注入其他 Spring bean。在这个代码中，它们用来注入设备服务 (DeviceService)。
 * */
/**
 * invokeOperation 方法通过反射机制调用 CoffeeMakerService 中对应的方法，并在调用前后触发相应的事件（开始、完成、出错）。
 * */