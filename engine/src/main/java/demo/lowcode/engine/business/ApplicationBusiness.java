package demo.lowcode.engine.business;

import demo.lowcode.engine.model.ActionMeta;
import demo.lowcode.engine.model.ProcessMeta;
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

    public void loadApplication(String appPath) {
        // 读取该应用的json文件

        // 新增应用，获取应用信息

        // 加载该应用流程
        addProcess("ConferenceService","会议服务", "EventRegistration", "GuestReception", null);
    }

    public void addProcess(String processId, String processName, String parentProcessId, String applicationId, List<ActionMeta> actionMetas) {

    }

    public List<ProcessMeta> getProcesses(String applicationId) {
        ProcessMeta processMeta = new ProcessMeta("EventRegistration", "活动申报", "GuestReception", "", null);
        ProcessMeta processMeta2 = new ProcessMeta("ConferenceService","会议服务", "GuestReception", "EventRegistration", processBusiness.getActionMetaList("ConferenceService"));
        return new ArrayList<>(Arrays.asList(processMeta, processMeta2));
    }
}
