package demo.lowcode.engine.business;

import demo.lowcode.common.Action;
import demo.lowcode.common.Device;
import demo.lowcode.common.EventListener;
import demo.lowcode.common.Service;
import demo.lowcode.engine.model.DeviceMeta;
import demo.lowcode.engine.util.JavaDynamicCompiler;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@org.springframework.stereotype.Service
public class ActionBusiness {
    @Resource
    ScenarioBusiness scenarioBusiness;

    public Action getAction(String type, String objectId) throws Exception {
        if (Objects.equals(type, "Device")) {
            // 获取mock数据
            List<DeviceMeta> deviceMetaList = scenarioBusiness.getDeviceMetaList("");
            DeviceMeta deviceMeta = deviceMetaList.get(0);

            // 绑定具体设备
            String deviceType = deviceMeta.getMainObject().getDeviceType();
            String serviceType = deviceMeta.getMainObject().getServiceType();
            Class<?> deviceClass = Class.forName("demo.lowcode.device."+deviceType.toLowerCase()+"."+deviceType);
            Device device = (Device) deviceClass.getConstructor().newInstance();

            Class<?> serviceClass = Class.forName("demo.lowcode.device."+deviceType.toLowerCase()+".service.type."+serviceType);
            Service service = (Service) serviceClass.getConstructor().newInstance();
            device.bindService(service);

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
        }
        return null;
    }
}
