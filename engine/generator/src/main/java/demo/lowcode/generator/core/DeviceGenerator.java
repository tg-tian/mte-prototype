package demo.lowcode.generator.core;

import demo.lowcode.common.Command;
import demo.lowcode.common.Param;
import demo.lowcode.generator.util.FileUtil;
import demo.lowcode.generator.util.JsonUtil;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeviceGenerator {
    String deviceType;
    String definitionPath;
    String baseDir;
    String parentPath = "D:/projects/ubml/mte-prototype/engine/";
    String groupId = "lowcode.device";
    String version = "1.0.0";

    List<Command> commands = new ArrayList<>();
    List<String> services = new ArrayList<>();

    public DeviceGenerator(String deviceType, String definitionPath) {
        this.deviceType = deviceType;
        this.definitionPath = definitionPath;
        groupId = groupId + "." + deviceType.toLowerCase();
        baseDir = parentPath+deviceType+"/src/main/java/" + groupId.replace(".", "/");
        File base = new File(baseDir);
        if (!base.exists()){
            ProjectGenerator projectGenerator = new ProjectGenerator(parentPath, groupId, deviceType.toLowerCase(), version);
            projectGenerator.generate();
        }

        // 读取操作
        readDeviceInformation();
    }

    public void generate(){
        // 创建事件Event
        generateEvent();

        // 创建设备Service
        generateService();

        // TODO:创建设备主类

        // 读取具体事件json
        EventGenerator eventGenerator = new EventGenerator();
        for (Command command: commands){
            // 在工作区生成java文件
            eventGenerator.generateEventFile(groupId, definitionPath, definitionPath+"definitions/events/"+FileUtil.capitalizeFirstLetter(command.getCommandName())+"Event.json");
            // 在设备项目下生成java文件(just测试java文件的准确性，实际使用中这里不需要而是直接copy工作区文件)
            eventGenerator.generateEventFile(groupId, baseDir, definitionPath+"definitions/events/"+FileUtil.capitalizeFirstLetter(command.getCommandName())+"Event.json");
        }

        // 读取服务json
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        for (String service: services) {
            // 在工作区生成java文件
            serviceGenerator.generateServiceFile(groupId, deviceType, definitionPath, definitionPath+"definitions/services/"+FileUtil.capitalizeFirstLetter(service)+".json");
            // 在设备项目下生成java文件(just测试java文件的准确性，实际使用中这里不需要而是直接copy工作区文件)
            serviceGenerator.generateServiceFile(groupId, deviceType, baseDir, definitionPath+"definitions/services/"+FileUtil.capitalizeFirstLetter(service)+".json");
        }
    }

    public void generateEvent(){
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
        services.clear();

        // 读取 JSON 文件内容
        String jsonContent = JsonUtil.readJson(definitionPath+"definitions/"+deviceType+".json");

        // 将 JSON 内容解析为 JSONObject
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);

            JSONArray refList = jsonObject.getJSONArray("refs");
            JSONArray serviceList = null;
            for (int i=0;i<refList.length();i++){
                if (Objects.equals(refList.getJSONObject(i).getString("refID"), "services")){
                    serviceList = refList.getJSONObject(i).getJSONArray("refPath");
                    break;
                }
            }

            if (serviceList!=null){
                for (int i=0;i<serviceList.length();i++){
                    services.add(serviceList.getJSONObject(i).getString("name"));
                }
            }

            JSONArray commandList = jsonObject.getJSONArray("commands");
            for (int i = 0; i < commandList.length(); i++) {
                JSONObject commandObject = commandList.getJSONObject(i);

                Command command = new Command();
                command.setCommandName(commandObject.getString("code"));

                JSONArray inputParamList = commandObject.getJSONArray("inputParam");
                List<Param> params = new ArrayList<>();
                for (int j=0; j<inputParamList.length(); j++){
                    JSONObject inputObject = inputParamList.getJSONObject(j);

                    Param param = new Param();
                    param.setCode(inputObject.getString("code"));
                    param.setName(inputObject.getString("name"));
                    String type = inputObject.getString("type");
                    if (Objects.equals(type, "Enum")){
                        param.setType("String");
                        List<Object> options = new ArrayList<>();
                        JSONArray optionList = inputObject.getJSONArray("options");
                        for (int k=0;k<optionList.length();k++){
                            Object item = optionList.get(k);
                            options.add(item);
                        }
                        param.setOptional(options);
                    }else {
                        param.setType(type);
                    }

                    params.add(param);
                }
                command.setInputParam(params);

                command.setOutputParam(commandObject.getString("outputParam"));

                commands.add(command);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void generateService(){
        String className = deviceType+"Service";
        StringBuilder eventContent = new StringBuilder();

        // package
        String packageName = groupId+".service";
        eventContent.append("package ").append(packageName).append(";\n\n");

        // import
        eventContent.append("import demo.lowcode.common.*;\n");
        eventContent.append("import demo.lowcode.common.device.DeviceService;\n");
        eventContent.append("import java.util.*;\n");

        eventContent.append("\n");

        // class
        eventContent.append("public class ").append(className).append(" extends DeviceService {\n");
        eventContent.append("    @Override\n");
        eventContent.append("    public Map<String, Object> getProperty() {\n")
                .append("        return new HashMap<>();\n")
                .append("    }\n\n");
        for (Command command: commands){
            eventContent.append("    public ").append(command.getOutputParam()).append(" ").append(FileUtil.lowercaseFirstLetter(command.getCommandName())).append("(");
            for (int i=0;i<command.getInputParam().size();i++){
                Param param = command.getInputParam().get(i);
                eventContent.append(param.getType()).append(" ").append(param.getCode());

                if (i<command.getInputParam().size()-1){
                    eventContent.append(", ");
                }
            }
            eventContent.append(") {\n");
            eventContent.append("        throw new RuntimeException(\"NoSupportedOperation\");\n");
            eventContent.append("    }\n\n");
        }
        eventContent.append("}");

        FileUtil.writeFile(baseDir+"/service/"+className+".java", String.valueOf(eventContent));
    }

    public void generateMain() {

    }
}
