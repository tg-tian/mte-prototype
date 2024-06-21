package demo.lowcode.engine;

import demo.lowcode.engine.business.DomainBusiness;
import demo.lowcode.engine.business.ProcessBusiness;
import demo.lowcode.engine.business.ScenarioBusiness;
import demo.lowcode.engine.model.ActionMeta;
import demo.lowcode.engine.model.DeviceMeta;
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

    @Test
    public void processExecute() throws IOException {
        //TODO：新增ApplicationBusiness,一个Application会包含多个Process，loadApplicationDefinition()
        //TODO：每个Process表示要执行的功能，该功能会包含一系列Action依次执行,executeProcess()
        //TODO: Action节点不要绑定某个具体的设备而是根据要执行的操作分配给该场景下的合适的设备来执行,修改getAction()

        // 领域层面：获取定义（设备类型及其对应的功能服务列表）
        domainBusiness.loadDomain(System.getProperty("user.dir")+"\\definition\\SmartBuilding.do");

        List<String> deviceTypeList = domainBusiness.getDeviceTypeList("smartBuilding");
        System.out.println("当前领域设备类型列表："+deviceTypeList.toString());
        String deviceType = deviceTypeList.get(0);
        List<String> serviceTypeList = domainBusiness.getServiceType(deviceType, "smartBuilding");
        System.out.println("当前领域设备"+deviceType+"对应的功能服务列表："+serviceTypeList.toString());
        String serviceType = serviceTypeList.get(0);

        // 场景层面：加载具体设备以及场景地图
        scenarioBusiness.loadScenario(System.getProperty("user.dir")+"\\definition\\BuildingA.sce");

        Map<String, String> scenarioMap = scenarioBusiness.getScenarioMap("scenarioId");
        System.out.println("当前场景地图："+scenarioMap.toString());
        List<DeviceMeta> deviceList = scenarioBusiness.getDeviceMetaList("scenarioId");
        System.out.println("当前场景具体设备列表："+deviceList.toString());

        // 应用层面：加载流程Action列表
        processBusiness.loadProcess("processPath");// 类型为Device的Action会绑定到具体设备并添加注册事件（仅在DeviceMeta属性中标明，并没有实际操作）

        List<ActionMeta> actionMetaList = processBusiness.getActionMetaList("processId");
        System.out.println("流程的action元数据列表："+actionMetaList.toString());

        // 执行（根据DeviceMeta进行实际的绑定具体设备、注册事件）
        try {
            processBusiness.executeProcess("processId");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
