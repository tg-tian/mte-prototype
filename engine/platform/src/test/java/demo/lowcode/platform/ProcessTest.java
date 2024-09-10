package demo.lowcode.platform;

import demo.lowcode.platform.business.*;
import demo.lowcode.platform.model.ActionMeta;
import lowcode.device.component.business.DeviceComponentBusiness;
import lowcode.device.component.model.DeviceMeta;
import demo.lowcode.platform.model.DomainMeta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProcessTest {
    @Autowired
    DomainBusiness domainBusiness;
    @Autowired
    ScenarioBusiness scenarioBusiness;
    @Autowired
    ProcessBusiness processBusiness;
    @Autowired
    ApplicationBusiness applicationBusiness;
    @Autowired
    DeviceComponentBusiness deviceComponentBusiness;

    @Test
    public void loadProcess() throws IOException {
        // 领域层面：获取定义（设备类型及其对应的功能服务列表）
        DomainMeta domainMeta = domainBusiness.loadDomain(System.getProperty("user.dir")+"\\definition\\SmartBuilding.do");

//        deviceComponentBusiness.loadDevice("CoffeeMaker/CoffeeMaker.json");

        List<String> deviceTypeList = domainBusiness.getDeviceTypeList("SmartBuilding");
        System.out.println("当前领域设备类型列表："+deviceTypeList.toString());

        // 场景层面：加载具体设备以及场景地图
        scenarioBusiness.loadScenario(System.getProperty("user.dir")+"\\definition\\BuildingA.sce");

        Map<String, String> scenarioMap = scenarioBusiness.getScenarioMap("scenarioId");
        System.out.println("当前场景地图："+scenarioMap.toString());
        List<DeviceMeta> deviceList = scenarioBusiness.getDeviceMetaList("scenarioId");
        System.out.println("当前场景具体设备列表："+deviceList.toString());

        // 应用层面：加载应用流程列表
        applicationBusiness.loadApplication(System.getProperty("user.dir")+"\\definition\\GuestReception.app");
        System.out.println("来访接待应用的流程列表："+applicationBusiness.getProcesses("GuestReception"));

        List<ActionMeta> actionMetaList = processBusiness.getActionMetaList("ConferenceService");
        System.out.println("会议服务流程的action元数据列表："+actionMetaList.toString());
    }

    @Test
    public void executeLiteFlowProcess() {
        // 执行（根据DeviceMeta进行实际的绑定具体设备、注册事件）
        try {
            processBusiness.executeLiteFlow("ConferenceService", new HashMap<>(){{put("makeCoffee", new HashMap<>(){{put("coffeeType", "美式");}});}});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
