package lowcode.device.generator.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.Command;
import demo.lowcode.common.Param;
import demo.lowcode.common.util.JsonUtils;
import demo.lowcode.common.util.FileUtil;
import demo.lowcode.common.util.StringUtil;
import demo.lowcode.common.CommonConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeviceGenerator {
    String deviceType;
    String definitionPath;
    String baseDir;
    String parentPath;
    String groupId = "lowcode.device";
    String version = "1.0.0";
    String resourceDir;

    List<Command> commands = new ArrayList<>();
    List<String> events = new ArrayList<>();
    List<String> services = new ArrayList<>();

    ProjectGenerator projectGenerator;
    EventGenerator eventGenerator;
    ServiceGenerator serviceGenerator;

    public DeviceGenerator(String deviceType, String definitionPath) {
        this.deviceType = deviceType;
        this.definitionPath = definitionPath;
        this.parentPath = CommonConfig.getProjectPath();

        // 读取操作
        readDeviceInformation();

        projectGenerator = new ProjectGenerator(parentPath, groupId, deviceType.toLowerCase(), version);
        eventGenerator = new EventGenerator();
        serviceGenerator = new ServiceGenerator();

        groupId = groupId + "." + deviceType.toLowerCase();
        baseDir = parentPath+deviceType+"/src/main/java/" + groupId.replace(".", "/");
        resourceDir = parentPath+deviceType+"/src/main/resources/";
    }

    public void initial() {
        projectGenerator.generate();
    }

    public void clear() {
        FileUtil.deleteDir(new File(parentPath+deviceType));
    }

    public void generate() throws Exception {
        clear();

        initial();

        // 创建事件Event
        generateEvent();

        // 创建设备Service
        generateService();

        // 创建设备主类
        generateMain();

        generateServiceJson();

        generateEventJson();
    }

    public void buildAndPackage() throws Exception {
        projectGenerator.buildAndPackage(new File(parentPath+deviceType.toLowerCase()));
    }

    public void copyJarFile() throws Exception{
        String filePath = parentPath+deviceType.toLowerCase()+"/target/"+deviceType.toLowerCase()+"-"+version+".jar";
        // 生成的jar包拷贝到工作目录
        File source = new File(parentPath+deviceType.toLowerCase()+"/target/"+deviceType.toLowerCase()+"-"+version+".jar");

        if (!source.exists()){
            throw new RuntimeException("拷贝失败，jar包不存在");
        }

        File dest = new File(definitionPath + "generate");
        if (!dest.exists()){
            dest.mkdirs();
        }
        FileUtil.copyFile(source, dest);
    }

    public void generateEventJson() throws Exception{
        FileUtil.deleteDirFiles(new File(resourceDir+"events"));

        // 读取具体事件json
        for (Command command: commands){
            File source = new File(definitionPath+"definitions/events/"+StringUtil.capitalizeFirstLetter(command.getCommandCode())+"Event.json");
            File dest = new File(resourceDir+"events");
            if (!source.exists()){
                throw new RuntimeException("事件"+command.getCommandCode()+"定义打包失败，文件不存在");
            }
            if (!dest.exists()){
                dest.mkdirs();
            }
            FileUtil.copyFile(source, dest);
        }
    }

    public void generateServiceJson(){
//        FileUtil.deleteDir(new File(definitionPath+"generate/service"));

        for (String service: services) {
            // 在工作区生成java文件
            // serviceGenerator.generateServiceFile(groupId, deviceType, definitionPath, definitionPath+"definitions/services/"+StringUtil.capitalizeFirstLetter(service)+".json");
            // 在设备项目下生成java文件
            serviceGenerator.generateServiceFile(groupId, deviceType, baseDir, definitionPath+"definitions/services/"+StringUtil.capitalizeFirstLetter(service)+".json");
        }
    }

    private void generateEvent(){
        String className = deviceType+"Event";

        // package
        String packageName = groupId+".event";

        String eventContent = "package " + packageName + ";\n\n" +

                // import
                "import demo.lowcode.common.*;\n" +
                "import lombok.*;\n" +
                "\n" +

                // class
                "@Data\n" + "@EqualsAndHashCode(callSuper = true)\n" +
                "public class " + className + " extends Event {\n" +
                "    private Object[] information;\n" +
                "    public " + className + "(String message, int code) {\n" +
                "        setMessage(message);\n" +
                "        setCode(code);\n" +
                "    }\n" +
                "    public " + className + "(String message, int code, Object... args) {\n" +
                "        setMessage(message);\n" +
                "        setCode(code);\n" +
                "        this.information = args;\n" +
                "    }\n" +
                "}";

        FileUtil.writeFile(baseDir+"/event/"+className+".java", eventContent);
    }

    private void readDeviceInformation(){
        commands.clear();
        events.clear();
        services.clear();

        String jsonContent = JsonUtils.readJson(definitionPath+"definitions/"+deviceType+".json");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonContent);

            // 获取services列表
            JsonNode refList = jsonNode.path("refs");
            JsonNode serviceList = null;
            for (JsonNode refNode : refList) {
                if ("services".equals(refNode.path("refID").asText())) {
                    serviceList = refNode.path("refPath");
                    break;
                }
            }

            if (serviceList != null) {
                for (JsonNode serviceNode : serviceList) {
                    services.add(serviceNode.path("code").asText());
                }
            }

            // 获取commands列表
            JsonNode commandList = jsonNode.path("commands");
            for (JsonNode commandNode : commandList) {

                Command command = new Command();
                command.setCommandCode(commandNode.path("code").asText());
                command.setCommandName(commandNode.path("name").asText());

                JsonNode inputParamList = commandNode.path("inputParam");
                List<Param> params = new ArrayList<>();
                for (JsonNode inputNode : inputParamList) {
                    Param param = new Param();
                    param.setCode(inputNode.path("code").asText());
                    param.setName(inputNode.path("name").asText());
                    param.setType(inputNode.path("type").asText());

                    List<String> options = new ArrayList<>();
                    JsonNode optionList = inputNode.path("options");
                    if (optionList.isArray()) {
                        for (JsonNode optionNode : optionList) {
                            options.add(optionNode.asText());
                        }
                    }
                    param.setOptions(options);
                    params.add(param);
                }
                command.setInputParam(params);

                command.setOutputParam(commandNode.path("outputParam").asText());

                commands.add(command);

                // 获取events列表
                JsonNode eventList = commandNode.path("events");
                for (JsonNode eventNode : eventList) {
                    events.add(eventNode.asText());
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void generateService(){
        String className = deviceType+"Service";
        StringBuilder serviceContent = new StringBuilder();

        // package
        String packageName = groupId+".service";
        serviceContent.append("package ").append(packageName).append(";\n\n");

        // import
        serviceContent.append("import demo.lowcode.common.*;\n");
        serviceContent.append("import demo.lowcode.common.device.DeviceService;\n");
        serviceContent.append("import java.util.*;\n");

        serviceContent.append("\n");

        // class
        serviceContent.append("public class ").append(className).append(" extends DeviceService {\n");
        serviceContent.append("    @Override\n");
        serviceContent.append("    public Map<String, Object> getProperty() {\n")
                .append("        return new HashMap<>();\n")
                .append("    }\n\n");
        for (Command command: commands){
            serviceContent.append("    public ").append(command.getOutputParam()).append(" ").append(StringUtil.lowercaseFirstLetter(command.getCommandCode())).append("(");
            for (int i=0;i<command.getInputParam().size();i++){
                Param param = command.getInputParam().get(i);
                serviceContent.append(param.getType()).append(" ").append(param.getCode());

                if (i<command.getInputParam().size()-1){
                    serviceContent.append(", ");
                }
            }
            serviceContent.append(") {\n");
            serviceContent.append("        throw new RuntimeException(\"NoSupportedOperation\");\n");
            serviceContent.append("    }\n\n");
        }
        serviceContent.append("}");

        FileUtil.writeFile(baseDir+"/service/"+className+".java", String.valueOf(serviceContent));
    }

    private void generateMain() {
        StringBuilder classContent = new StringBuilder();

        // package
        classContent.append("package ").append(groupId).append(";\n\n");

        // import
        classContent.append("import demo.lowcode.common.*;\n");
        classContent.append("import demo.lowcode.common.EventListener;\n");
        classContent.append("import demo.lowcode.common.device.*;\n");
        classContent.append("import ").append(groupId).append(".event.*;").append("\n\n");
        classContent.append("import ").append(groupId).append(".service.*;").append("\n\n");
        classContent.append("import org.springframework.stereotype.Component;\n");
        classContent.append("import java.util.*;\n");
        classContent.append("import java.io.*;\n");
        classContent.append("import java.lang.reflect.*;\n");

        classContent.append("\n");

        /* class */
        classContent.append("@Component\n");
        classContent.append("public class ").append(deviceType).append(" extends Device{\n");

        // attribute
        classContent.append("    private Map<String, List<EventListener>> beforeEventListeners = new HashMap<>();\n");
        classContent.append("    private Map<String, List<EventListener>> afterEventListeners = new HashMap<>();\n");
        classContent.append("    private Map<String, List<EventListener>> errorEventListeners = new HashMap<>();\n");
        classContent.append("    private String definitionPath = \"").append(definitionPath).append("\";\n");

        // constructor
        classContent.append("    public ").append(deviceType).append("() {\n");
        List<String> commandNameList = new ArrayList<>();
        for (Command command: commands) {
            commandNameList.add("\""+command.getCommandCode()+"\"");
        }
        List<String> eventNameList = new ArrayList<>();
        for (String event: events) {
            eventNameList.add("\""+event+"\"");
        }
        classContent.append("        setOperations(").append("Arrays.asList(").append(String.join(", ", commandNameList)).append("));\n");
        classContent.append("        setEvents(").append("Arrays.asList(").append(String.join(", ", eventNameList)).append("));\n");
        classContent.append("    }\n");

        // eventMethod
        classContent.append("    private void onStart(String operationType, ").append(deviceType).append("Event event) {\n")
                .append("        List<EventListener> listeners = beforeEventListeners.get(operationType);\n")
                .append("        if (listeners != null) {\n")
                .append("            for (EventListener listener: listeners) {\n")
                .append("                if (event != null){\n")
                .append("                    listener.handleEvent(event);\n")
                .append("                }else {\n")
                .append("                    listener.handleEvent();\n")
                .append("                }\n")
                .append("            }\n")
                .append("        }\n")
                .append("    }\n\n");

        classContent.append("    private void onComplete(String operationType, ").append(deviceType).append("Event event) {\n")
                .append("        List<EventListener> listeners = afterEventListeners.get(operationType);\n")
                .append("        if (listeners != null) {\n")
                .append("            for (EventListener listener: listeners) {\n")
                .append("                if (event != null){\n")
                .append("                    listener.handleEvent(event);\n")
                .append("                }else {\n")
                .append("                    listener.handleEvent();\n")
                .append("                }\n")
                .append("            }\n")
                .append("        }\n")
                .append("    }\n\n");

        classContent.append("    private void onError(String operationType, ").append(deviceType).append("Event event) {\n")
                .append("        List<EventListener> listeners = errorEventListeners.get(operationType);\n")
                .append("        if (listeners != null) {\n")
                .append("            for (EventListener listener: listeners) {\n")
                .append("                if (event != null){\n")
                .append("                    listener.handleEvent(event);\n")
                .append("                }else {\n")
                .append("                    listener.handleEvent();\n")
                .append("                }\n")
                .append("            }\n")
                .append("        }\n")
                .append("    }\n\n");

        classContent.append("""
                    @Override
                    public void addEventListener(String operation, String eventType, EventListener eventHandler) {
                        if (eventType.equals("onError")) {
                            errorEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
                        }else if (eventType.equals("onComplete")){
                            afterEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
                        }else if (eventType.equals("onStart")){
                            beforeEventListeners.computeIfAbsent(operation, k -> new ArrayList<>()).add(eventHandler);
                        }
                    }

                """);

        // executeMethod
        classContent.append("""
                    @Override
                    public int invokeOperation(String operation, Object... args) {
                        try {
                            onStart(operation, null);
                            boolean flag = false;
                            if (deviceService instanceof\s""").append(deviceType).append("Service) {\n")
                .append("                try {\n")
                .append("                    Method method;\n")
                .append("                    if (args.length > 0) {\n")
                .append("                        Class<?>[] parameterTypes = new Class<?>[args.length];\n")
                .append("                        for (int i = 0; i < args.length; i++) {\n")
                .append("                            parameterTypes[i] = args[i].getClass();\n")
                .append("                        }\n")
                .append("                        method = ").append(deviceType).append("Service.class.getDeclaredMethod(operation, parameterTypes);\n")
                .append("                        method.invoke(deviceService, args);\n")
                .append("                    } else {\n")
                .append("                        method = ").append(deviceType).append("Service.class.getDeclaredMethod(operation);\n")
                .append("                        method.invoke(deviceService);\n")
                .append("                    }\n")
                .append("                    flag = true;\n")
                .append("                } catch (InvocationTargetException e) {\n")
                .append("                    // ignore\n")
                .append("                }\n")
                .append("            }\n")
                .append("            if (!flag){\n")
                .append("                throw new RuntimeException(\"There is no available service to handle operation \"+operation);\n")
                .append("            }\n")
                .append("            if (args.length > 0){\n")
                .append("               onComplete(operation, new ").append(deviceType).append("Event(operation, 200, args));\n")
                .append("            }else {\n")
                .append("               onComplete(operation, null);\n")
                .append("            }\n")
                .append("            return 0;\n")
                .append("        } catch (Exception e) {\n")
                .append("            onError(operation, new ").append(deviceType).append("Event(\"方法或事件执行失败：\"+e.getMessage(), 400));\n")
                .append("            e.printStackTrace();\n")
                .append("            return 1;\n")
                .append("        }\n")
                .append("    }\n\n");

        classContent.append("""
                    @Override
                    public ActionExecResult execute(Object... args) {
                        ActionExecResult actionExecResult = new ActionExecResult();
                        if (args.length >= 1 && args[0] instanceof String) {
                            String operationName = (String) args[0];
                            int code = 0;
                            if (args.length > 1){
                                Object[] operationArgs = (Object[]) args[1];
                                code = invokeOperation(operationName, operationArgs);
                            }else {
                                code = invokeOperation(operationName);
                            }
                            actionExecResult.setCode(code);
                        } else {
                            actionExecResult.setCode(2);
                        }
                        return actionExecResult;
                    }

                """);

        classContent.append("}");

        FileUtil.writeFile(baseDir+"/"+deviceType+".java", String.valueOf(classContent));
    }
}
