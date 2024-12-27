package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import demo.lowcode.common.Action;
import demo.lowcode.common.ActionExecResult;
import demo.lowcode.common.CommonConfig;
import demo.lowcode.common.EventListener;
import demo.lowcode.common.device.Device;
import demo.lowcode.common.device.DeviceService;
import demo.lowcode.common.util.FileUtil;
import demo.lowcode.common.util.JavaDynamicCompiler;
import demo.lowcode.common.util.StringUtil;
import demo.lowcode.platform.model.ActionMeta;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lowcode.device.component.business.MockBusiness;
import lowcode.device.component.model.DeviceConnectService;
import lowcode.device.component.model.DeviceMeta;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@org.springframework.stereotype.Service
public class ActionBusiness {
    @Resource
    ScenarioBusiness scenarioBusiness;

    @Resource
    MockBusiness mockBusiness;

    public Action getAction(String scenarioId, String scePath, ActionMeta actionMeta, Map<String, Object> executeArgs) throws Exception {
        if (Objects.equals(actionMeta.getType(), "Device")){
            String deviceType = actionMeta.getObjectId();
            // 根据设备类型（objectId）获取该场景中对应的设备实例
            List<DeviceMeta> deviceMetaList = scenarioBusiness.getDeviceMetaList(scenarioId);

            for (DeviceMeta deviceMeta: deviceMetaList){
                DeviceConnectService service = deviceMeta.getMainObject().getService();

                // 判断该设备所包含的功能服务是否能够执行execParam的操作，若能够执行则进行如下操作，否则看下一个设备是否满足
                if (Objects.equals(deviceMeta.getMainObject().getType(), deviceType)){
                    List<String> operations = service.getOperations();
                    boolean canExec = false;
                    for (String operation: operations){
                        if (operation.contains(actionMeta.getExecParam())) {
                            canExec = true;
                            break;
                        }
                    }
                    if (!canExec){
                        continue;
                    }
                }else {
                    continue;
                }

                // 绑定具体设备
                String jarPath = scePath+"device/"+deviceType+"/"+deviceType.toLowerCase()+"-1.0.0.jar";
                URLClassLoader classLoader = JavaDynamicCompiler.loadJar(jarPath);
                Class<?> deviceClass = classLoader.loadClass("lowcode.device."+deviceType.toLowerCase()+"."+deviceType);
                Device device = (Device) deviceClass.getConstructor().newInstance();

                // 设备绑定对应服务
                String packageName = "lowcode.device."+deviceType.toLowerCase()+".generate.service."+service.getName();
                Class<?> serviceClass = classLoader.loadClass(packageName);
                DeviceService deviceService = (DeviceService) serviceClass.getConstructor(String.class).newInstance(service.getUri());
                device.bindService(deviceService);

                // 获取执行参数
                if (executeArgs != null){
                    Map<String, Object> properties = deviceService.getProperty();
                    boolean canExecArg = true;
                    for (Map.Entry<String, Object> arg : executeArgs.entrySet()) {
                        if (properties.containsKey(arg.getKey())){
                            Object deviceProperty = properties.get(arg.getKey());
                            if (deviceProperty instanceof List<?>){
                                // 判断服务执行操作时是否支持相关属性
                                System.out.println(deviceProperty);

                                System.out.println(arg.getValue());

                                if (!((List<?>) deviceProperty).contains(arg.getValue())){
                                    canExecArg = false;
                                }
                            }
                        }
                    }
                    if (!canExecArg){
                        continue;
                    }
                }

                // 设备注册事件
                Class<?> eventClass = Class.forName("demo.lowcode.common.Event");
//                Class<?> eventClass = classLoader.loadClass("lowcode.device."+deviceType.toLowerCase()+".event."+deviceType+"Event");
                // 对不同的操作获取对应的事件
                List<String> operations = device.getOperations();
                for (String operation: operations){
                    // 获取操作对应的事件controller文件
                    String jsonFile = "events/"+StringUtil.capitalizeFirstLetter(operation)+"Event.json";
                    JsonNode jsonNode = FileUtil.readJarJson(jarPath, jsonFile);
                    String eventFile = jsonNode.path("eventPath").asText();

                    String eventFilePath = scePath+"device/"+deviceType+"/event/java/"+eventFile;
                    String outputDir = scePath+"device/"+deviceType+"/event/class/";
                    URLClassLoader controllerClassLoader = JavaDynamicCompiler.compileAndLoadEventFile(jarPath, eventFilePath, outputDir, classLoader);

                    String controllerPackageName = "lowcode.device."+deviceType.toLowerCase()+".generate.event."+StringUtil.capitalizeFirstLetter(operation)+"Controller";
                    Class<?> controllerClass = controllerClassLoader.loadClass(controllerPackageName);

                    // 对其中每一个事件方法添加eventListener
                    JsonNode eventArrayNode = jsonNode.path("eventList");
                    if (eventArrayNode.isArray()) {
                        for (JsonNode eventNode : eventArrayNode) {
                            String eventType = eventNode.path("type").asText();

                            Method method = null;
                            String methodSignature = eventNode.path("signature").asText();
                            String methodName = methodSignature.substring(0, methodSignature.indexOf('('));
                            String methodParam = methodSignature.substring(methodSignature.indexOf('(')+1, methodSignature.indexOf(')'));
                            if (methodParam.equals("")){
                                // 尝试获取无参数的方法
                                method = controllerClass.getDeclaredMethod(methodName);
                            }else {
                                // 尝试获取带参数的方法
                                method = controllerClass.getDeclaredMethod(methodName, eventClass);
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
                            device.addEventListener(operation, eventType, eventListener);
                        }
                    }
                }
                return device;
            }
        }else if(Objects.equals(actionMeta.getType(), "Default")) {
            return new Action() {
                @Override
                public ActionExecResult execute(Object... args) {
                    return null;
                }
            };
        }
        return null;
    }

    public ActionExecResult executeAction(Action action, Object... args) {
        System.out.println("----------开始执行----------");
        mockBusiness.setCurrentOperation((String) args[0]);
        mockBusiness.setStatus("忙碌");
        ActionExecResult result = action.execute(args);
        if (result == null || result.getCode() == 0){
            mockBusiness.setCurrentOperation("");
            mockBusiness.setStatus("空闲");
        }else {
            mockBusiness.setStatus("错误");
        }
        System.out.println("----------结束执行----------");
        return result;
    }
}
