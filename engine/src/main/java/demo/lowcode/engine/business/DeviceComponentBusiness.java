package demo.lowcode.engine.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.engine.entity.BrandService;
import demo.lowcode.engine.entity.Event;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import demo.lowcode.common.Command;
import demo.lowcode.common.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class DeviceComponentBusiness {
    @Value("${definitionPath}")
    private String definitionPath;

    public void loadDevice(String devicePath) throws IOException {
        System.out.println("开始加载设备类型");
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(input);
        definitionPath = properties.getProperty("definitionPath");
        File file = new File(definitionPath + devicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

        String deviceType = rootNode.path("type").asText();

        // 读取commands数组
        JsonNode commandsNode = rootNode.path("commands");
        List<Command> commands = new ArrayList<>();
        if (commandsNode.isArray()) {
            for (JsonNode command : commandsNode) {
                String commandName = command.path("name").asText();
                String outputParam = command.path("outputParam").asText();
                List<Param> inputParam = new ArrayList<>();
                JsonNode inputNodes = command.path("inputParam");
                if (inputNodes.isArray()) {
                    for (JsonNode inputNode : inputNodes) {
                        Param param = new Param(inputNode.path("code").asText(), inputNode.path("name").asText(), inputNode.path("type").asText());
                        inputParam.add(param);
                    }
                }
                Command command1 = new Command();//commandName, inputParam, outputParam);
                commands.add(command1);
            }
        }

        // 读取events数组
        JsonNode eventsNode = rootNode.path("events");
        List<String> events = new ArrayList<>();
        if (eventsNode.isArray()) {
            for (JsonNode event : eventsNode) {
                events.add(event.asText());
            }
        }
        System.out.println("读取json文件成功");

        generateDeviceProject(deviceType, commands, events);
        System.out.println("结束加载设备类型");
    }

    private void generateDeviceProject(String deviceType, List<Command> commands, List<String> events) throws IOException {
        String baseDir = "src/main/java/demo/lowcode/device/" + deviceType.toLowerCase();
        System.out.println("生成目录");
        createDirectoryStructure(baseDir);

        System.out.println("生成主文件");
        generateJavaFile(baseDir, deviceType, commands, events);
        System.out.println("生成service文件");
        generateServiceFile(baseDir, deviceType, commands);
        System.out.println("生成Event文件");
        generateEventFile(baseDir, deviceType);
    }

    private void generateServiceFile(String baseDir, String deviceType, List<Command> commands) throws IOException {
        String serviceClassName = deviceType + "Service";
        File serviceJavaFile = new File(baseDir + "/service", serviceClassName + ".java");
        try (FileWriter writer = new FileWriter(serviceJavaFile)) {
            writer.write("package demo.lowcode.device." + deviceType.toLowerCase() + ".service;\n\n");
            writer.write("import demo.lowcode.common.device.DeviceService;\n");
            writer.write("public class " + serviceClassName + " extends DeviceService {\n");
            for (Command command : commands) {
                List<Param> inputParams = new ArrayList<>();//command.getInputParam();
                StringBuilder param = new StringBuilder();
                for (Param inputParam : inputParams) {
                    param.append(inputParam.getType()).append(" ").append(inputParam.getName()).append(", ");
                }
                if (param.length() > 0) {
                    param.delete(param.length() - 2, param.length()); // 删除最后一个多余的逗号和空格
                }
                writer.write("");//    public " + command.getOutputParam() + " " + command.getCommandName() + "("+param+") {\n");
                writer.write("        throw new RuntimeException(\"NoSupportedOperation\");\n");
                writer.write("    }\n");
            }
            writer.write("}\n");
        }
    }

    private void generateEventFile(String baseDir, String deviceType) throws IOException {
        String eventClassName = deviceType + "Event";
        File eventJavaFile = new File(baseDir + "/event", eventClassName + ".java");
        try (FileWriter writer = new FileWriter(eventJavaFile)) {
            writer.write("package demo.lowcode.device." + deviceType.toLowerCase() + ".event;\n\n");
            writer.write("import demo.lowcode.common.Event;\n");
            writer.write("import lombok.AllArgsConstructor;\n\n");
            writer.write("@AllArgsConstructor\n");
            writer.write("public class " + eventClassName + " extends Event {\n");
            writer.write("    public " + eventClassName + "(String message, int code) {\n");
            writer.write("        setMessage(message);\n");
            writer.write("        setCode(code);\n");
            writer.write("    }\n");
            writer.write("}\n");
        }
    }

    private void generateJavaFile(String baseDir, String deviceType, List<Command> commands, List<String> events) throws IOException {
        File javaFile = new File(baseDir, deviceType + ".java");

        List<String> imports = Arrays.asList(
                "import demo.lowcode.common.ActionExecResult;",
                "import demo.lowcode.common.device.Device;",
                "import demo.lowcode.common.device.DeviceService;",
                "import demo.lowcode.common.EventListener;",
                "import demo.lowcode.device." + deviceType.toLowerCase() + ".event." + deviceType + "Event;",
                "import demo.lowcode.device." + deviceType.toLowerCase() + ".service." + deviceType + "Service;",
                "import java.lang.reflect.InvocationTargetException;",
                "import java.lang.reflect.Method;",
                "import java.util.*;"
        );
        ;


        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write("package demo.lowcode.device." + deviceType.toLowerCase() + ";\n\n");
            for (String importSentence : imports) {
                writer.write(importSentence + "\n");
            }
            writer.write("public class " + deviceType + " extends Device {\n");

            writer.write("    private Map<String, List<EventListener>> beforeEventListeners = new HashMap<>();\n");
            writer.write("    private Map<String, List<EventListener>> afterEventListeners = new HashMap<>();\n");
            writer.write("    private Map<String, List<EventListener>> errorEventListeners = new HashMap<>();\n\n");

            writer.write("    public " + deviceType + "() {\n");
            writer.write("        List<String> commands = Arrays.asList(\"");
            writer.write("        setOperations(commands);\n\n");
            writer.write("        List<String> events = Arrays.asList(\"" + String.join("\", \"", events) + "\");\n");
            writer.write("        setEvents(events);\n");
            writer.write("    }\n\n");

            writer.write("    private void onStart(String operationType) {\n");
            writer.write("        List<EventListener> listeners = beforeEventListeners.get(operationType);\n");
            writer.write("        if (listeners != null) {\n");
            writer.write("            for (EventListener listener: listeners) {\n");
            writer.write("                listener.handleEvent();\n");
            writer.write("            }\n");
            writer.write("        }\n");
            writer.write("    }\n\n");
            writer.write("    private void onComplete(String operationType) {\n");
            writer.write("        List<EventListener> listeners = afterEventListeners.get(operationType);\n");
            writer.write("        if (listeners != null) {\n");
            writer.write("            for (EventListener listener : listeners) {\n");
            writer.write("                listener.handleEvent();\n");
            writer.write("            }\n");
            writer.write("        }\n");
            writer.write("    }\n\n");
            writer.write("    private void onError(String operationType, " + deviceType + "Event event) {\n");
            writer.write("        List<EventListener> listeners = errorEventListeners.get(operationType);\n");
            writer.write("        if (listeners != null) {\n");
            writer.write("            for (EventListener listener : listeners) {\n");
            writer.write("                listener.handleEvent(event);\n");
            writer.write("            }\n");
            writer.write("        }\n");
            writer.write("    }\n\n");

            writer.write("    @Override\n");
            writer.write("    public void addEventListener(String eventName, EventListener eventHandler) {\n");
            writer.write("        if (eventName.startsWith(\"on\") && eventName.endsWith(\"Error\")) {\n");
            writer.write("            String operation = eventName.substring(2, eventName.length() - 5);\n");
            writer.write("            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);\n");
            writer.write("            errorEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);\n");
            writer.write("        } else if (eventName.startsWith(\"on\") && eventName.endsWith(\"Complete\")) {\n");
            writer.write("            String operation = eventName.substring(2, eventName.length() - 8);\n");
            writer.write("            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);\n");
            writer.write("            afterEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);\n");
            writer.write("        } else if (eventName.startsWith(\"on\") && eventName.endsWith(\"Start\")) {\n");
            writer.write("            String operation = eventName.substring(2, eventName.length() - 5);\n");
            writer.write("            operation = Character.toLowerCase(operation.charAt(0)) + operation.substring(1);\n");
            writer.write("            beforeEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);\n");
            writer.write("        }\n");
            writer.write("    }\n\n");

            writer.write("    @Override\n");
            writer.write("    public int invokeOperation(String operation, Object... args) {\n");
            writer.write("        try {\n");
            writer.write("            onStart(operation);\n");
            writer.write("            boolean flag = false;\n");
            writer.write("            for (DeviceService deviceService : deviceServices) {\n");
            writer.write("                if (deviceService instanceof " + deviceType + "Service) {\n");
            writer.write("                    try {\n");
            writer.write("                        Method method;\n");
            writer.write("                        if (args.length > 0) {\n");
            writer.write("                            Class<?>[] parameterTypes = new Class<?>[args.length];\n");
            writer.write("                            for (int i = 0; i < args.length; i++) {\n");
            writer.write("                                parameterTypes[i] = args[i].getClass();\n");
            writer.write("                            }\n");
            writer.write("                            method = " + deviceType + "Service.class.getDeclaredMethod(operation, parameterTypes);\n");
            writer.write("                            method.invoke(deviceService, args);\n");
            writer.write("                        } else {\n");
            writer.write("                            method = " + deviceType + "Service.class.getDeclaredMethod(operation);\n");
            writer.write("                            method.invoke(deviceService);\n");
            writer.write("                        }\n");
            writer.write("                        flag = true;\n");
            writer.write("                        break;\n");
            writer.write("                    } catch (InvocationTargetException e) {\n");
            writer.write("                        // ignore\n");
            writer.write("                    }\n");
            writer.write("                }\n");
            writer.write("            }\n");
            writer.write("            if (!flag) {\n");
            writer.write("                throw new RuntimeException(\"There is no available service to handle operation \" + operation);\n");
            writer.write("            }\n");
            writer.write("            onComplete(operation);\n");
            writer.write("            return 0;\n");
            writer.write("        } catch (Exception e) {\n");
            writer.write("            onError(operation, new " + deviceType + "Event(e.getMessage(), 400));\n");
            writer.write("            e.printStackTrace();\n");
            writer.write("            return 1;\n");
            writer.write("        }\n");
            writer.write("    }\n\n");

            writer.write("    @Override\n");
            writer.write("    public ActionExecResult execute(Object... args) {\n");
            writer.write("        ActionExecResult actionExecResult = new ActionExecResult();\n");
            writer.write("        if (args.length >= 1 && args[0] instanceof String) {\n");
            writer.write("            String operationName = (String) args[0];\n");
            writer.write("            Object[] operationArgs = Arrays.copyOfRange(args, 1, args.length);\n");
            writer.write("            int code = invokeOperation(operationName, operationArgs);\n");
            writer.write("            actionExecResult.setCode(code);\n");
            writer.write("        } else {\n");
            writer.write("            actionExecResult.setCode(2);\n");
            writer.write("        }\n");
            writer.write("        return actionExecResult;\n");
            writer.write("    }\n");

            writer.write("}\n");
        }
    }

    private void createDirectoryStructure(String baseDir) throws IOException {
        Files.createDirectories(Paths.get(baseDir, "event"));
        Files.createDirectories(Paths.get(baseDir, "service/type"));
    }


    /**
     * 设备拓展功能实现
     * loadParam : 加载 输入 和 输出 参数
     * loadEvent : 加载 事件的基本信息
     */
    @ApiOperation(value = "加载参数", notes = "直接载入 设备信息类 的 参数 属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devicePath",value="设备名称json访问路径",required=true),
            @ApiImplicitParam(name = "command_code", value = "设备支持的操作代码",required = true),
    })
    private Param loadInputParam(String devicePath,  String command_code) throws IOException {
        // paramType : ["inputParam","outputParam"]
        // command_code : ["Start","MakeCoffee",......]
        File file = new File(devicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * ObjectMapper 是 Jackson 库中用于处理 JSON 数据的核心类。以下是 ObjectMapper 的常见功能：
         * 1.将对象转换为 JSON 字符串：使用 writeValueAsString(Object value) 方法，可以将 Java 对象转换为 JSON 格式的字符串。
         * 2.将 JSON 字符串转换为对象：使用 readValue(String content, Class<T> valueType) 方法，可以将 JSON 字符串转换为指定类型的 Java 对象。
         * 3.将 JSON 字符串转换为集合或泛型对象：使用 readValue(String content, TypeReference<T> valueTypeRef) 方法，可以将 JSON 字符串转换为泛型对象或集合类型。
         * 4.从文件中读取 JSON 数据：使用 readValue(File src, Class<T> valueType) 方法，可以从文件中读取 JSON 数据并转换为 Java 对象。
         * 5.将对象写入文件为 JSON：使用 writeValue(File resultFile, Object value) 方法，可以将 Java 对象转换为 JSON 并写入文件中。
         * */
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode commands = rootNode.path("commands");
        Param param = new Param();

        if (commands.isArray() && commands.path(command_code).isArray()) {
            //获得对应的操作的json目录
            JsonNode command = commands.path(command_code);
            JsonNode Params = command.path("in").path(0); //获取inputParam的第一个节点

            if (Params.isArray()) {
                String code = Params.path("code").asText();
                String name = Params.path("name").asText();
                String type = Params.path("type").asText();
                /**
                List<String> options = new ArrayList<>();
                JsonNode option_s = Params.path("options");
                for (JsonNode opt : option_s) {
                    options.add(opt.asText());
                }*/
                param.Change(code, name, type);
            }
        }
        return param;
    }

    @ApiOperation(value = "加载事件", notes = "加载事件的具体关联信息,读取文件：“操作名称” + Event.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="eventPath",value="事件json访问路径",required=true),
    })
    private List<Event> loadEvent(String eventPath) throws IOException {
        File file = new File(eventPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode events = rootNode.path("eventList");
        List<Event> eventList = new ArrayList<>();
        if (events.isArray()) {
            for (JsonNode event : events) {
                String name = event.path("name").asText();
                String description = event.path("description").asText();
                String type = event.path("type").asText();
                Event new_event = new Event(name,description,type);
                eventList.add(new_event);
            }
        }
        return eventList;
    }

    @ApiOperation(value = "加载操作", notes = "加载操作基本信息,读取文件：“设备名称”.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devicePath",value="设备名称json访问路径",required=true),
    })
    private List<Command> loadCommand(String devicePath) throws  IOException{
        File file = new File(devicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode commands = rootNode.path("commands");
        List<Command> commandList = new ArrayList<>();
        if(commands.isArray()){
            for(JsonNode command:commands){
                String code = command.path("code").asText();
                String name = command.path("name").asText();
                Command command1 = new Command(code,name);
                commandList.add(command1);
            }
        }
        return commandList;
    }

    @ApiOperation(value = "加载服务", notes = "加载操作绑定基本信息,读取文件：“服务名称”.json ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="servicePath",value="服务名称json访问路径",required=true),
    })
    private BrandService loadService(String servicePath) throws IOException{
        File file = new File(servicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        String code = rootNode.path("code").asText();
        String description = rootNode.path("description").asText();
        return new BrandService(code,description);
    }
}
