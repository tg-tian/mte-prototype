package lowcode.device.component.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import demo.lowcode.common.Command;
import demo.lowcode.common.CommonConfig;
import demo.lowcode.common.Param;
import demo.lowcode.common.device.DeviceProperty;
import demo.lowcode.common.util.FileUtil;
import demo.lowcode.common.util.JsonUtils;
import demo.lowcode.common.util.StringUtil;
import lowcode.device.component.entity.BrandService;
import lowcode.device.component.entity.DeviceEvent;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/* 新增时写入json */
@Service
public class AddDeviceTypeBusiness {

    // 根据基本信息初始化设备类型
    public void initDeviceType(String deviceCode, String deviceName, String imageUrl){
        // TODO: 判断是否已存在（查数据库）
        if (deviceCode == null) {
            throw new RuntimeException("传入deviceCode不能为空");
        }
        String baseDir = CommonConfig.getDefinitionPath()+deviceCode;
        if (!new File(baseDir).exists()){
            // 1、新建文件夹及文件
//        FileUtil.deleteDir(new File(baseDir));
            new File(baseDir+"/definitions").mkdirs();
            new File(baseDir+"/definitions/events").mkdirs();
            new File(baseDir+"/definitions/services").mkdirs();

            // 2、写入json内容
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 创建根节点 (ObjectNode)
                ObjectNode jsonObject = objectMapper.createObjectNode();
                jsonObject.put("code", deviceCode);
                jsonObject.put("name", deviceName);
                jsonObject.put("type", "Device");
                jsonObject.put("img_link", imageUrl);

                // 创建 "refs" 数组 (ArrayNode)
                ArrayNode refs = objectMapper.createArrayNode();
                // 创建 eventRef 对象并添加到 refs
                ObjectNode eventRef = objectMapper.createObjectNode();
                eventRef.put("refID", "events");
                eventRef.set("refPath", objectMapper.createArrayNode());
                refs.add(eventRef);

                // 创建 serviceRef 对象并添加到 refs
                ObjectNode serviceRef = objectMapper.createObjectNode();
                serviceRef.put("refID", "services");
                serviceRef.set("refPath", objectMapper.createArrayNode());
                refs.add(serviceRef);

                jsonObject.set("refs", refs);

                // 创建 "properties" 数组 (ArrayNode)
                ArrayNode properties = objectMapper.createArrayNode();
                jsonObject.set("properties", properties);

                // 创建 "commands" 数组 (ArrayNode)
                ArrayNode commands = objectMapper.createArrayNode();
                jsonObject.set("commands", commands);

                File outputFile = new File(baseDir + "/definitions/" + deviceCode + ".json");
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, jsonObject);
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
    }


    // 向设备类型中添加操作
    public void addCommand(String deviceType, Command command){
        // TODO: 判断该类型是否存在

        // 读取对应的json文件
        String baseDir = CommonConfig.getDefinitionPath()+deviceType+"/definitions/";
        String jsonContent = JsonUtils.readJson(baseDir+deviceType+".json");

        // TODO: 检查操作是否存在
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 读取现有的 JSON 内容
            JsonNode jsonObject = objectMapper.readTree(jsonContent);

            // 创建新的 commandObject
            ObjectNode commandObject = objectMapper.createObjectNode();
            commandObject.put("code", command.getCommandCode());
            commandObject.put("name", command.getCommandName());

            // 创建 inputParams 数组
            ArrayNode input = objectMapper.createArrayNode();
            if (command.getInputParam() != null) {
                for (Param param : command.getInputParam()) {
                    ObjectNode paramObject = objectMapper.createObjectNode();
                    paramObject.put("code", param.getCode());
                    paramObject.put("name", param.getName());
                    paramObject.put("type", param.getType());
                    paramObject.set("options", objectMapper.valueToTree(param.getOptions()));
                    paramObject.put("default", param.getDefaultValue() != null ? param.getDefaultValue() : "");
                    input.add(paramObject);
                }
            }
            commandObject.set("inputParams", input);
            commandObject.put("outputParam", command.getOutputParam());
            commandObject.set("events", objectMapper.createArrayNode());
            commandObject.set("services", objectMapper.createArrayNode());

            // 将 commandObject 添加到 commands 数组
            ((ArrayNode) jsonObject.get("commands")).add(commandObject);

            // 添加 event 信息
            String eventCode = StringUtil.capitalizeFirstLetter(command.getCommandCode()) + "Event";
            ArrayNode ref = (ArrayNode) jsonObject.get("refs");
            ArrayNode eventRefs = (ArrayNode) ref.get(0).get("refPath");
            ObjectNode event = objectMapper.createObjectNode();
            event.put("code", eventCode);
            event.put("path", "events/" + eventCode + ".json");
            eventRefs.add(event);

            // 生成对应的事件文件
            File eventFile = new File(baseDir + "events/" + eventCode + ".json");
            if (!eventFile.exists()) {
                eventFile.createNewFile();
            }

            File jsonFile = new File(baseDir + deviceType + ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, jsonObject);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    // 操作添加事件
    public void addEvent(String deviceType, String commandCode, DeviceEvent event){
        // 读取主文件，添加对应ref
        String baseDir = CommonConfig.getDefinitionPath()+deviceType+"/definitions/";
        String jsonContent = JsonUtils.readJson(baseDir+deviceType+".json");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonObject = objectMapper.readTree(jsonContent);

            // 获取 "commands" 数组
            ArrayNode commandsArray = (ArrayNode) jsonObject.get("commands");

            // 遍历 "commands" 数组，查找匹配的 commandCode
            for (int i = 0; i < commandsArray.size(); i++) {
                ObjectNode commandObject = (ObjectNode) commandsArray.get(i);

                // 如果找到匹配的 commandCode，添加事件到 "events" 数组
                if (commandObject.get("code").asText().equals(commandCode)) {
                    ArrayNode eventsArray = (ArrayNode) commandObject.get("events");
                    eventsArray.add(event.getName());  // 添加事件名称
                }
            }

            // 写入更新后的 JSON 文件
            File jsonFile = new File(baseDir + deviceType + ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 读取事件文件，添加事件信息
        // TODO:检查事件是否存在
        String eventCode = StringUtil.capitalizeFirstLetter(commandCode)+"Event";
        String eventContent = JsonUtils.readJson(baseDir+"events/"+eventCode+".json");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode eventObject;

            // 创建新的 event 信息
            ObjectNode newEvent = objectMapper.createObjectNode();
            newEvent.put("name", event.getName());
            newEvent.put("description", event.getDescription());
            newEvent.put("type", event.getType());
            newEvent.put("command", commandCode);
            newEvent.put("signature", "");
            newEvent.put("body", "");

            // 判断 eventContent 是否为空
            if (eventContent.isEmpty()) {
                eventObject = objectMapper.createObjectNode();
                eventObject.put("eventPath", eventCode + "Controller.java");

                // 创建新的 eventList 数组
                ArrayNode eventArray = objectMapper.createArrayNode();
                eventArray.add(newEvent);

                // 设置 eventList 到 eventObject
                eventObject.set("eventList", eventArray);
            } else {
                // 如果 eventContent 不为空，则读取已有的 JSON 内容
                JsonNode existingContent = objectMapper.readTree(eventContent);
                eventObject = (ObjectNode) existingContent;

                // 获取并更新 eventList
                ArrayNode eventArray = (ArrayNode) eventObject.get("eventList");
                eventArray.add(newEvent);
            }
            // 将更新后的 JSON 内容写入文件
            File eventFile = new File(baseDir + "events/" + eventCode + ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(eventFile, eventObject);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 初始化厂商json
    public void initService(String deviceType, BrandService service){
        String baseDir = CommonConfig.getDefinitionPath()+deviceType+"/definitions/";
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonObject = objectMapper.createObjectNode();
            jsonObject.put("code", service.getCode());
            jsonObject.put("name", service.getName());
            jsonObject.put("device", deviceType);
            jsonObject.put("description", service.getDescription());

            // 创建空的 parameters 和 commands 数组
            ArrayNode parametersArray = objectMapper.createArrayNode();
            ArrayNode commandsArray = objectMapper.createArrayNode();

            // 将空数组加入到 JSON 对象
            jsonObject.set("parameters", parametersArray);
            jsonObject.set("commands", commandsArray);

            // 写入文件
            File file = new File(baseDir + "services/" + service.getFilename());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }

        // 读取主文件，添加对应ref
        String jsonContent = JsonUtils.readJson(baseDir+deviceType+".json");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            ArrayNode refsArray = (ArrayNode) jsonNode.get("refs");

            // 创建新的 serviceRef 对象
            ObjectNode serviceRef = objectMapper.createObjectNode();
            serviceRef.put("code", service.getCode());
            serviceRef.put("path", "services/" + service.getFilename());

            // 向 refs 数组的第二个元素中添加新的 serviceRef
            ArrayNode refPathArray = (ArrayNode) refsArray.get(1).get("refPath");
            refPathArray.add(serviceRef);

            // 将更新后的 JSON 写回文件
            File file = new File(baseDir + deviceType + ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 前端编辑完厂商json
    public void editService(String deviceType, String serviceName){
        String baseDir = CommonConfig.getDefinitionPath()+deviceType+"/definitions/";
        // 读取服务文件，获取厂商信息
        String serviceContent = JsonUtils.readJson(baseDir+"services/"+serviceName+".json");
        List<String> commandList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode serviceObject = objectMapper.readTree(serviceContent);

            // 获取 commands 数组
            ArrayNode serviceMethod = (ArrayNode) serviceObject.get("commands");

            // 遍历 commands 数组，提取每个方法的 signature
            for (JsonNode method : serviceMethod) {
                String signature = method.get("signature").asText();

                // 提取方法签名中的方法名
                if (signature != null && signature.contains("(")) {
                    commandList.add(signature.substring(0, signature.indexOf("(")));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        // 读取主文件，添加对应ref
        String jsonContent = JsonUtils.readJson(baseDir+deviceType+".json");
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            ArrayNode commandsArray = (ArrayNode) jsonNode.get("commands");
            for (int i=0;i< commandsArray.size();i++){
                ObjectNode c = (ObjectNode) commandsArray.get(i);
                if (commandList.contains(c.get("code").asText())){
                    ArrayNode services = (ArrayNode)c.get("services");

                    boolean serviceExists = IntStream.range(0, services.size())
                            .mapToObj(index -> {
                                try {
                                    return services.get(index).asText();
                                } catch (Exception e) {
                                    throw new RuntimeException("Error while processing JSON", e);
                                }
                            })
                            .anyMatch(serviceName::equals);

                    // 如果 serviceName 不在服务列表中，则添加
                    if (!serviceExists) {
                        services.add(serviceName);
                    }
                }
            }
            File file = new File(baseDir + deviceType + ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProperty(String deviceType, List<DeviceProperty> properties) {
        // 读取对应的json文件
        String baseDir = CommonConfig.getDefinitionPath()+deviceType+"/definitions/";
        String jsonContent = JsonUtils.readJson(baseDir+deviceType+".json");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 读取现有的 JSON 内容
            JsonNode jsonObject = objectMapper.readTree(jsonContent);
            ArrayNode propertiesNode = (ArrayNode) jsonObject.get("properties");

            for (DeviceProperty property : properties) {
                // 判断属性是否存在
                boolean isExist = false;
                for (int i=0; i<propertiesNode.size();i++){
                    if (Objects.equals(propertiesNode.get(i).path("identifier").asText(), property.getIdentifier())){
                        isExist = true;
                    }
                }
                if (isExist) continue;

                // 创建新的 propertyObject
                ObjectNode propertyObject = objectMapper.createObjectNode();

                propertyObject.put("identifier", property.getIdentifier());
                propertyObject.put("name", property.getName());
                propertyObject.put("accessMode", property.getAccessMode());
                propertyObject.put("enableValidate", property.isEnableValidate());

                propertyObject.set("validateParams", objectMapper.createArrayNode());
                // 数据类型
                ObjectNode dataTypeNode = objectMapper.createObjectNode();
                dataTypeNode.put("type", property.getDeviceDataType().getType());
                ObjectNode specsNode = objectMapper.createObjectNode();
                for (String key: property.getDeviceDataType().getSpecs().keySet()){
                    specsNode.put(key, property.getDeviceDataType().getSpecs().get(key));
                }
                dataTypeNode.set("specs", specsNode);
                propertyObject.set("dataType", dataTypeNode);

                // 将 propertyObject 添加到 properties 数组
                propertiesNode.add(propertyObject);
            }

            File jsonFile = new File(baseDir + deviceType + ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, jsonObject);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
