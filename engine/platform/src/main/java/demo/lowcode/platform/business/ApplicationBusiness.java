package demo.lowcode.platform.business;

import demo.lowcode.common.CommonConfig;
import demo.lowcode.platform.model.ActionMeta;
import demo.lowcode.platform.model.ProcessMeta;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ApplicationBusiness {
    @Resource
    private ProcessBusiness processBusiness;

    // 应用增删改查
    public void addApplication() {

    }

    // TODO: load application definition
    // TODO: 解释引擎：可能要加载多个app，不同的app应该有不同的入口
    public void loadApplication(String appPath) {
        // 读取该应用的json文件

        // 新增应用，获取应用信息

        // 加载该应用流程
        addProcess("CoffeeService","咖啡服务", "", "SelfServeCoffee", null);
    }

    public void addProcess(String processId, String processName, String parentProcessId, String applicationId, List<ActionMeta> actionMetas) {

    }

    public List<ProcessMeta> getProcesses(String applicationId) {
        ProcessMeta processMeta2 = new ProcessMeta("CoffeeService","咖啡服务", "SelfServeCoffee", "",  processBusiness.getActionMetaList("CoffeeService", CommonConfig.getWorkspacePath()+"SmartBuilding/PhysicalBuilding/application/SelfServeCoffee/process/"));

        // TODO：初始化RTProcess

        return new ArrayList<>(Arrays.asList(processMeta2));
    }
}
