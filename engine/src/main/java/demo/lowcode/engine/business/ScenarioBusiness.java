package demo.lowcode.engine.business;

import demo.lowcode.engine.model.DeviceInformation;
import demo.lowcode.engine.model.DeviceMeta;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@org.springframework.stereotype.Service
public class ScenarioBusiness {
    @Value("${eventPath}")
    private String eventFilePath;

    // 场景增删改查
    public void addScenario() {

    }

    public void loadScenario(String scePath) {
        // 读取该场景的json文件

        // 新增场景，获取场景信息

        // 加载该场景地图信息
        addScenarioMap(null, "");

        // 加载该场景具体设备
        addDeviceMeta("", "CoffeeMaker", "AService", null);
    }

    public void addScenarioMap(Map<String, String> map, String scenarioId) {

    }

    public Map<String, String> getScenarioMap(String scenarioId) {
        return new HashMap<>() {{
            put("楼层", "2");
            put("房间号", "D2009");
        }};
    }

    public void addDeviceMeta(String scenarioId, String deviceType, String serviceType, DeviceInformation deviceInformation) {

    }

    public List<DeviceMeta> getDeviceMetaList(String scenarioId) {
        scenarioId = "BuildingA";
        String deviceType = "CoffeeMaker";
        String controllerJavaFile = eventFilePath+"MakeCoffeeController.java";
        Map<String, String> eventMap = new HashMap<>(){{
            put("onMakeCoffeeStart", "prepare");
            put("onMakeCoffeeComplete", "sendMessage");
            put("onCheckError", "errorAlert");
        }};

        // 咖啡机器人A
        String serviceType = "AService";
        DeviceInformation deviceInformation = new DeviceInformation("deviceId", null, serviceType, deviceType, eventMap, controllerJavaFile);
        DeviceMeta deviceMeta = new DeviceMeta("deviceId", "咖啡机器人A", scenarioId, deviceInformation);

        // 咖啡机器人B
        String serviceType2 = "BService";
        DeviceInformation deviceInformation2 = new DeviceInformation("deviceId", null, serviceType2, deviceType, eventMap, controllerJavaFile);
        DeviceMeta deviceMeta2 = new DeviceMeta("deviceId2", "咖啡机器人B", scenarioId, deviceInformation2);
        return new ArrayList<>(Arrays.asList(deviceMeta, deviceMeta2));
    }
}
