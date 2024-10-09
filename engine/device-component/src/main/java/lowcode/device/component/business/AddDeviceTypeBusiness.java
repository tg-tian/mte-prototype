package lowcode.device.component.business;

import demo.lowcode.common.Command;
import demo.lowcode.common.CommonConfig;
import demo.lowcode.common.Param;
import demo.lowcode.common.util.FileUtil;
import demo.lowcode.common.util.JsonUtils;
import demo.lowcode.common.util.StringUtil;
import lowcode.device.component.entity.BrandService;
import lowcode.device.component.entity.DeviceEvent;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
        String baseDir = CommonConfig.getDefinitionPath()+deviceCode;
        if (!new File(baseDir).exists()){
            // 1、新建文件夹及文件
//        FileUtil.deleteDir(new File(baseDir));
            new File(baseDir+"/definitions").mkdirs();
            new File(baseDir+"/definitions/events").mkdirs();
            new File(baseDir+"/definitions/services").mkdirs();

            // 2、写入json内容
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", deviceCode);
                jsonObject.put("name", deviceName);
                jsonObject.put("type", "Device");
                jsonObject.put("img_link", imageUrl);

                JSONArray refs = new JSONArray();
                JSONObject eventRef = new JSONObject();
                eventRef.put("refID", "events");
                eventRef.put("refPath", new JSONArray());
                refs.put(eventRef);
                JSONObject serviceRef = new JSONObject();
                serviceRef.put("refID", "services");
                serviceRef.put("refPath", new JSONArray());
                refs.put(serviceRef);
                jsonObject.put("refs", refs);

                JSONArray commands = new JSONArray();
                jsonObject.put("commands", commands);
                FileUtil.writeFile(baseDir+"/definitions/"+deviceCode+".json", jsonObject.toString(4));
            }catch (JSONException e){
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
        try {
            // 向文件中添加操作信息
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONObject commandObject = new JSONObject();
            commandObject.put("code", command.getCommandCode());
            commandObject.put("name", command.getCommandName());
            JSONArray input = new JSONArray();
            if (command.getInputParam() != null){
                for (Param param: command.getInputParam()){
                    JSONObject paramObject = new JSONObject();
                    paramObject.put("code", param.getCode());
                    paramObject.put("name", param.getName());
                    paramObject.put("type", param.getType());
                    paramObject.put("options", new JSONArray(param.getOptions()));
                    paramObject.put("default", param.getDefaultValue()!=null ? param.getDefaultValue():"");
                    input.put(paramObject);
                }
            }
            commandObject.put("inputParams", input);
            commandObject.put("outputParam", command.getOutputParam());
            commandObject.put("events", new JSONArray());
            commandObject.put("services", new JSONArray());
            jsonObject.getJSONArray("commands").put(commandObject);

            String eventCode = StringUtil.capitalizeFirstLetter(command.getCommandCode())+"Event";
            JSONArray ref = jsonObject.getJSONArray("refs");
            JSONArray eventRefs = ref.getJSONObject(0).getJSONArray("refPath");
            JSONObject event = new JSONObject();
            event.put("code", eventCode);
            event.put("path", "events/"+eventCode+".json");
            eventRefs.put(event);

            // 生成对应的事件文件
            FileUtil.createFile(baseDir+"events/"+ eventCode+".json");
            FileUtil.writeFile(baseDir+deviceType+".json", jsonObject.toString(4));
        }catch (JSONException e){
            throw new RuntimeException(e.getMessage());
        }
    }


    // 操作添加事件
    public void addEvent(String deviceType, String commandCode, DeviceEvent event){
        // 读取主文件，添加对应ref
        String baseDir = CommonConfig.getDefinitionPath()+deviceType+"/definitions/";
        String jsonContent = JsonUtils.readJson(baseDir+deviceType+".json");
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray jsonArray = jsonObject.getJSONArray("commands");
            for (int i=0;i< jsonArray.length();i++){
                JSONObject c = jsonArray.getJSONObject(i);
                if (Objects.equals(c.getString("code"), commandCode)){
                    c.getJSONArray("events").put(event.getName());
                }
            }
            FileUtil.writeFile(baseDir+deviceType+".json", jsonObject.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 读取事件文件，添加事件信息
        // TODO:检查事件是否存在
        String eventCode = StringUtil.capitalizeFirstLetter(commandCode)+"Event";
        String eventContent = JsonUtils.readJson(baseDir+"events/"+eventCode+".json");
        try {
            JSONObject eventObject;

            JSONObject newEvent = new JSONObject();
            newEvent.put("name", event.getName());
            newEvent.put("description", event.getDescription());
            newEvent.put("type", event.getType());
            newEvent.put("command", commandCode);
            newEvent.put("signature", "");
            newEvent.put("body", "");

            if (Objects.equals(eventContent, "")){
                eventObject = new JSONObject();
                eventObject.put("eventPath", eventCode+"Controller.java");
                JSONArray eventArray = new JSONArray();
                eventArray.put(newEvent);
                eventObject.put("eventList", eventArray);
            }else {
                eventObject = new JSONObject(eventContent);
                JSONArray eventArray = eventObject.getJSONArray("eventList");
                eventArray.put(newEvent);
            }
            FileUtil.writeFile(baseDir+"events/"+ eventCode+".json", eventObject.toString(4));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    // 初始化厂商json
    public void initService(String deviceType, BrandService service){
        String baseDir = CommonConfig.getDefinitionPath()+deviceType+"/definitions/";
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", service.getCode());
            jsonObject.put("name", service.getName());
            jsonObject.put("device", deviceType);
            jsonObject.put("description", service.getDescription());
            jsonObject.put("parameters", new JSONArray());
            jsonObject.put("commands", new JSONArray());
            FileUtil.writeFile(baseDir+"services/"+service.getFilename(), jsonObject.toString(4));
        }catch (JSONException e){
            e.printStackTrace();
        }

        // 读取主文件，添加对应ref
        String jsonContent = JsonUtils.readJson(baseDir+deviceType+".json");
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray jsonArray = jsonObject.getJSONArray("refs");
            JSONObject serviceRef = new JSONObject();
            serviceRef.put("code", service.getCode());
            serviceRef.put("path", "services/"+service.getFilename());
            jsonArray.getJSONObject(1).getJSONArray("refPath").put(serviceRef);
            FileUtil.writeFile(baseDir+deviceType+".json", jsonObject.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 前端编辑完厂商json
    public void editService(String deviceType, String serviceName){
        String baseDir = CommonConfig.getDefinitionPath()+deviceType+"/definitions/";
        // 读取服务文件，获取厂商信息
        String serviceContent = JsonUtils.readJson(baseDir+"services/"+serviceName+".json");
        List<String> commandList = new ArrayList<>();
        try {
            JSONObject serviceObject = new JSONObject(serviceContent);
            JSONArray serviceMethod = serviceObject.getJSONArray("commands");
            for (int i=0;i<serviceMethod.length();i++){
                JSONObject method = serviceMethod.getJSONObject(i);
                String signature = method.getString("signature");
                commandList.add(signature.substring(0, signature.indexOf("(")));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        // 读取主文件，添加对应ref
        String jsonContent = JsonUtils.readJson(baseDir+deviceType+".json");
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray jsonArray = jsonObject.getJSONArray("commands");
            for (int i=0;i< jsonArray.length();i++){
                JSONObject c = jsonArray.getJSONObject(i);
                if (commandList.contains(c.getString("code"))){
                    JSONArray services = c.getJSONArray("services");

                    boolean serviceExists = IntStream.range(0, services.length())
                            .mapToObj(index -> {
                                try {
                                    return services.getString(index);
                                } catch (JSONException e) {
                                    throw new RuntimeException("Error while processing JSON", e);
                                }
                            })
                            .anyMatch(serviceName::equals);

                    // 如果 serviceName 不在服务列表中，则添加
                    if (!serviceExists) {
                        services.put(serviceName);
                    }
                }
            }
            FileUtil.writeFile(baseDir+deviceType+".json", jsonObject.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
