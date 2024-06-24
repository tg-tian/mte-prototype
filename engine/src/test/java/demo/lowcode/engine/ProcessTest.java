package demo.lowcode.engine;

import demo.lowcode.engine.business.ApplicationBusiness;
import demo.lowcode.engine.business.DomainBusiness;
import demo.lowcode.engine.business.ProcessBusiness;
import demo.lowcode.engine.business.ScenarioBusiness;
import demo.lowcode.engine.model.ActionMeta;
import demo.lowcode.engine.model.DeviceMeta;
import demo.lowcode.engine.model.DomainMeta;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ProcessTest {
    @Resource
    DomainBusiness domainBusiness;
    @Resource
    ScenarioBusiness scenarioBusiness;
    @Resource
    ProcessBusiness processBusiness;
    @Resource
    ApplicationBusiness applicationBusiness;

    @Test
    public void processExecute() throws IOException {
        //0624: Action节点不要绑定某个具体的设备而是根据要执行的操作分配给该场景下的合适的设备来执行,修改getAction()

        // 领域层面：获取定义（设备类型及其对应的功能服务列表）
        DomainMeta domainMeta = domainBusiness.loadDomain(System.getProperty("user.dir")+"\\definition\\SmartBuilding.do");

        Map<String, List<String>> deviceTypeList = domainBusiness.getDeviceTypeList("SmartBuilding");
        System.out.println("当前领域设备类型列表及对应的功能服务："+deviceTypeList.toString());

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

        // 执行（根据DeviceMeta进行实际的绑定具体设备、注册事件）
        try {
            processBusiness.executeProcess("ConferenceService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
