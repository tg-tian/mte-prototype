package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.platform.mapper.SceneMapper;
import demo.lowcode.platform.model.*;
import lowcode.device.component.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.*;

@org.springframework.stereotype.Service
public class ScenarioBusiness {

    private final SceneMapper sceneMapper;
    @Autowired
    public ScenarioBusiness(SceneMapper sceneMapper) {this.sceneMapper = sceneMapper;}

    @Value("${eventPath}")
    private String eventFilePath;

    public ScenarioJson addScearioJson(String scenarioId, String scenarioName, String domainId, String mapPath, List<Map<String,String>> mapList)
    {
        ScenarioJson scenarioJson = new ScenarioJson();
        scenarioJson.setScenarioId(scenarioId);
        scenarioJson.setScenarioName(scenarioName);
        scenarioJson.setDomainId(domainId);
        scenarioJson.setMapPath(mapPath);
        scenarioJson.setMaplist(mapList);

        return scenarioJson;
    }

    public Scenario_ResourceJson addResourceJson(List<Map<String,String>> devicesList, Map<String,Map<String,String>> devices_service){
        Scenario_ResourceJson scenario_resourceJson = new Scenario_ResourceJson();
        scenario_resourceJson.setDevicesList(devicesList);
        scenario_resourceJson.setDevices_service(devices_service);
        return scenario_resourceJson;
    }

    public Scenario_ResourceJson loadResourceJson() throws IOException{
        File file = new File("definition/BuildingA.sce"); //获取文件夹
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

        List<Map<String,String>> devicesList = new ArrayList<>();
        Map<String,Map<String,String>> devices_service = new HashMap<>();

        JsonNode devicesNodeList = rootNode.path("devices");
        for(JsonNode devicesNode: devicesNodeList){
            Map<String,String> devices_imf = new HashMap<>();

            String deviceId = devicesNode.path("deviceId").asText();
            devices_imf.put("deviceId",deviceId);
            devices_imf.put("deviceName", devicesNode.path("deviceName").asText());
            devices_imf.put("deviceType",devicesNode.path("deviceType").asText());
            devicesList.add(devices_imf);

            JsonNode serviceNode = devicesNode.path("service");
            Map<String,String> service_imf = new HashMap<>();
            service_imf.put("code",serviceNode.path("code").asText());
            service_imf.put("name",serviceNode.path("name").asText());
            service_imf.put("protocol",serviceNode.path("protocol").asText());
            service_imf.put("uri",serviceNode.path("uri").asText());
            service_imf.put("port",serviceNode.path("port").asText());
            devices_service.put(deviceId,service_imf);
        }

        return addResourceJson(devicesList,devices_service);
    }

    //获取领域基本信息json
    public ScenarioJson loadScenarioJson() throws IOException{

        File file = new File("definition/BuildingA.sce"); //获取文件夹
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

        //JsonNode ScenarioNodeList = rootNode.path("");
        String scenarioId = rootNode.path("scenarioId").asText();
        String scenarioName = rootNode.path("scenarioName").asText();
        String domainId = rootNode.path("domainId").asText();

        JsonNode mapNodeList = rootNode.path("map");
        String mapPath = mapNodeList.path("mapPath").asText();
        JsonNode mapListNodeList = mapNodeList.path("mapList");

        List<Map<String,String>> mapList = new ArrayList<>();
        for(JsonNode mapListNode : mapListNodeList){
            //声明楼层字典存储楼层信息
            Map<String,String> map_imf = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = mapListNode.fields();
            while (fieldsIterator.hasNext()){
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                map_imf.put(field.getKey(), field.getValue().asText());
            }
            mapList.add(map_imf);
        }

        return addScearioJson(scenarioId, scenarioName,domainId,mapPath,mapList);
    }



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

    public void addDeviceMeta(String scenarioId, String deviceType, String serviceType, DeviceTypeInformation deviceInformation) {

    }

    public List<DeviceMeta> getDeviceMetaList(String scenarioId) {
        // TODO: 获取具体设备实例（包括服务，不包括事件，事件在应用编辑时定义）

        scenarioId = "BuildingA";
        String deviceType = "CoffeeMaker";

        // 咖啡机器人A
        // TODO: 读数据库或读json文件获取品牌支持的操作
        List<String> operationsA = new ArrayList<>(Arrays.asList("getProperty()", "start()", "makeCoffee(String coffeeType)"));
        DeviceConnectService serviceA = new DeviceConnectService("AService", "http://aservice.coffee", operationsA);
        DeviceInformation deviceInformation = new DeviceInformation("deviceId", "咖啡机器人A", deviceType, serviceA);
        DeviceMeta deviceMeta = new DeviceMeta("deviceId", "咖啡机器人A", scenarioId, deviceInformation);

        // 咖啡机器人B
        List<String> operationsB = new ArrayList<>(Arrays.asList("getProperty()", "start()", "makeCoffee(String coffeeType)", "check()"));
        DeviceConnectService serviceB = new DeviceConnectService("BService", "http://bservice.coffee", operationsB);
        DeviceInformation deviceInformation2 = new DeviceInformation("deviceId2", "咖啡机器人B", deviceType, serviceB);
        DeviceMeta deviceMeta2 = new DeviceMeta("deviceId2", "咖啡机器人B", scenarioId, deviceInformation2);

        return new ArrayList<>(Arrays.asList(deviceMeta, deviceMeta2));
    }

    public Long loadSceneId(String sceneCode){
        return  sceneMapper.getSceneId(sceneCode);
    }
}
