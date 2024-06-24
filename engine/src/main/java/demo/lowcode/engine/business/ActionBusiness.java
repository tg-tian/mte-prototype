package demo.lowcode.engine.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.Action;
import demo.lowcode.common.extend.device.Device;
import demo.lowcode.common.EventListener;
import demo.lowcode.common.extend.device.DeviceService;
import demo.lowcode.engine.model.DeviceMeta;
import demo.lowcode.engine.util.JavaDynamicCompiler;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@org.springframework.stereotype.Service
public class ActionBusiness {
    @Resource
    ScenarioBusiness scenarioBusiness;

    @Value("${definitionPath}")
    private String definitionPath;

    public Action getAction(String type, String objectId, String execParam) throws Exception {
        if (Objects.equals(type, "Device")) {
            System.out.println("获取能够执行"+execParam+"的设备"+objectId);
            // 获取mock数据，这里应该是根据objectId也就是设备类型来找到对应的List
            List<DeviceMeta> deviceMetaList = scenarioBusiness.getDeviceMetaList("");

            for (DeviceMeta deviceMeta: deviceMetaList){
                try {
                    String deviceType = deviceMeta.getMainObject().getDeviceType();
                    String serviceType = deviceMeta.getMainObject().getServiceType();
                    // 判断该设备所包含的功能服务是否能够执行execParam的操作，若能够执行则进行如下操作，否则看下一个设备是否满足
                    if (Objects.equals(deviceType, objectId)){
                        ObjectMapper objectMapper = new ObjectMapper();
                        File file = new File(definitionPath+objectId+".json");
                        JsonNode rootNode = objectMapper.readTree(file);

                        // 读取service数组
                        JsonNode servicesNode = rootNode.path("services");
                        if (servicesNode.isArray()) {
                            boolean flag = true;
                            for (JsonNode service : servicesNode) {
                                if (Objects.equals(serviceType, service.path("name").asText())){
                                    JsonNode operationNode = service.path("operations");
                                    if (Objects.equals(operationNode.path(execParam).asText(), "")){
                                        flag = false;
                                    }
                                }
                            }
                            if (!flag) continue;
                        }
                    }else {
                        continue;
                    }

                    // 绑定具体设备
                    Class<?> deviceClass = Class.forName("demo.lowcode.device."+deviceType.toLowerCase()+"."+deviceType);
                    Device device = (Device) deviceClass.getConstructor().newInstance();

                    Class<?> serviceClass = Class.forName("demo.lowcode.device."+deviceType.toLowerCase()+".service.type."+serviceType);
                    DeviceService deviceService = (DeviceService) serviceClass.getConstructor().newInstance();
                    device.bindService(deviceService);

                    // 注册事件
                    Class<?> eventClass = Class.forName("demo.lowcode.device."+deviceType.toLowerCase()+".event."+deviceType+"Event");
                    Class<?> controllerClass = JavaDynamicCompiler.compileAndLoad(deviceMeta.getMainObject().getEventPath());
                    Map<String, String> eventMap = deviceMeta.getMainObject().getEventMap();
                    for (String key : eventMap.keySet()) {
                        Method method = null;
                        String methodName = eventMap.get(key);
                        try {
                            // 尝试获取带参数的方法
                            method = controllerClass.getDeclaredMethod(methodName, eventClass);
                        } catch (NoSuchMethodException e) {
                            // 尝试获取无参数的方法
                            method = controllerClass.getDeclaredMethod(methodName);
                        }
                        Method finalMethod = method;
                        EventListener eventListener = new EventListener() {
                            @SneakyThrows
                            @Override
                            public void handleEvent(Object... args) {
                                if (args.length == 0) {
                                    finalMethod.invoke(controllerClass.getDeclaredConstructor().newInstance());
                                }else {
                                    finalMethod.invoke(controllerClass.getDeclaredConstructor().newInstance(), args);
                                }
                            }
                        };
                        device.addEventListener(key, eventListener);
                    }

                    return device;
                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return null;
    }

    public void executeAction(Action action, Object... args) {
        System.out.println("----------开始执行----------");
        action.execute(args);
        System.out.println("----------结束执行----------");
    }
}
