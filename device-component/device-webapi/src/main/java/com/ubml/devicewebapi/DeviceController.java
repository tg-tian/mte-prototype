package com.ubml.devicewebapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.inspur.edp.lcm.metadata.api.service.MetadataService;
import com.ubml.devicemanager.ContentSerializer;
import com.ubml.devicemanager.DeviceMetadataContentManager;
import com.ubml.devicemodel.DeviceModelEntity;
import com.ubml.devicemodel.DeviceService;
import com.ubml.devicemodel.common.CommandEntity;
import com.ubml.devicemodel.common.EventEntity;
import com.ubml.devicemodel.common.ParameterEntity;
import io.iec.edp.caf.commons.utils.SpringBeanUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class DeviceController {
    private com.inspur.edp.lcm.metadata.api.service.MetadataService metadataService;

    /**
     * 元数据服务
     *
     * @return 获取元数据服务类实例
     */
    private MetadataService getMetadataService() {
        if (metadataService == null)
            metadataService = SpringBeanUtils.getBean(MetadataService.class);
        return metadataService;
    }

    // 保存事件json
    @Path("saveFile/event")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void saveEventJson(String deviceType, String operationName, String eventInfo){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(eventInfo);

            // 格式化JSON内容
            ObjectMapper formattedMapper = new ObjectMapper();
            formattedMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String formattedJson = formattedMapper.writeValueAsString(node);

            // 指定文件路径
            String filePath = deviceType+"/definitions/events/"+operationName+"Event.json";
            File file = new File(filePath);

            // 将格式化的JSON内容写入文件
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(formattedJson);
                System.out.println("文件已成功保存到: " + file.getAbsolutePath());
            }
//
//            JsonNode eventsNode = node.get("eventList");
//            List<EventEntity> events = new ArrayList<>();
//            if (eventsNode.isArray()) {
//                for (JsonNode eventNode : eventsNode) {
//                    String name = eventNode.get("name").asText();
//                    String description = eventNode.get("description").asText();
//                    String type = eventNode.get("type").asText();
//                    String command = eventNode.get("command").asText();
//                    String signature = eventNode.get("signature").asText();
//
//                    events.add(new EventEntity(name, description, type, command, signature));
//                }
//            }

        } catch (Exception e) {
            throw new RuntimeException("json结构异常，导致反序列化出错，错误信息请见内部异常", e);
        }
    }

    // 保存服务json
    @Path("saveFile/service")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void saveServiceJson(String deviceType, String serviceName, String serviceInfo){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(serviceInfo);

            // 格式化JSON内容
            ObjectMapper formattedMapper = new ObjectMapper();
            formattedMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String formattedJson = formattedMapper.writeValueAsString(node);

            // 指定文件路径
            String filePath = deviceType+"/definitions/services/"+serviceName+".json";
            File file = new File(filePath);

            // 将格式化的JSON内容写入文件
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(formattedJson);
                System.out.println("文件已成功保存到: " + file.getAbsolutePath());
            }

        } catch (Exception e) {
            throw new RuntimeException("json结构异常，导致反序列化出错，错误信息请见内部异常", e);
        }
    }

    // 初始化服务json
    @Path("initial/service")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void initialServiceJson(String deviceType, String serviceInfo){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(serviceInfo);
            String code = node.get("code").textValue();
            String name = node.get("name").textValue();
            String device = node.get("device").textValue();

            JsonNode commandsNode = node.get("commands");
            List<CommandEntity> commands = new ArrayList<>();
            if (commandsNode.isArray()) {
                for (JsonNode commandNode : commandsNode) {
                    String signature = commandNode.get("signature").asText();
                    String body = commandNode.get("body").asText();

                    commands.add(new CommandEntity(signature, body));
                }
            }

            JsonNode paramsNode = node.get("parameters");
            List<ParameterEntity> params = new ArrayList<>();
            if (paramsNode.isArray()) {
                for (JsonNode paramNode : paramsNode) {
                    String paramCode = paramNode.get("code").asText();
                    String paramType = paramNode.get("type").asText();
                    String paramDescription = paramNode.get("description").asText();

                    params.add(new ParameterEntity(paramCode, paramType, paramDescription));
                }
            }

            DeviceService deviceService = new DeviceService(code, name, device, commands, params);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("json结构异常，导致反序列化出错，错误信息请见内部异常", e);
        }
    }

    /**
     * 新建元数据
     *
     * @param metadataInfo 元数据基本信息
     * @return String
     */
    @Path("initial")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public String initial(String metadataInfo) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(metadataInfo);
            String type = node.get("type").textValue();
            String metadataId = node.get("metadataId").textValue();
            String metadataName = node.get("metadataName").textValue();
            String metadataCode = node.get("metadataCode").textValue();
            String metadataAssembly = node.get("metadataAssembly").textValue();

            DeviceModelEntity deviceModelEntity = initDeviceModelEntity(metadataId, metadataName, metadataCode, metadataAssembly, type);
            ContentSerializer serializer = new ContentSerializer();
            return serializer.Serialize(deviceModelEntity).toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json结构异常，导致反序列化出错，错误信息请见内部异常", e);
        }
    }

    /**
     * 初始化单值DeviceModelEntity的类型定义
     *
     * @param metadataID       元数据ID
     * @param metadataName     元数据名称
     * @param metadataCode     元数据编号
     * @param metadataAssembly 元数据集合
     * @param type             DeviceModelEntity
     * @return DeviceModelEntity
     */
    private DeviceModelEntity initDeviceModelEntity(String metadataID, String metadataName, String metadataCode, String metadataAssembly, String type) {
        DeviceModelEntity deviceModelEntity = new DeviceModelEntity();
        deviceModelEntity.setCode(metadataCode);
        deviceModelEntity.setName(metadataName);
        return deviceModelEntity;
    }

    /**
     * 获取设备模型实例
     *
     * @return String
     */
    @Path("entity/device/{deviceId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode getExtendConfigByConfigId(@PathParam("deviceId") String deviceId) {
        DeviceMetadataContentManager deviceManager = new DeviceMetadataContentManager();
        return deviceManager.getDeviceModelEntity();
    }

}
