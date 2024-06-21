package demo.lowcode.engine.business;

import demo.lowcode.common.Action;
import demo.lowcode.engine.model.ActionMeta;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessBusiness {
    @Resource
    ActionBusiness actionBusiness;

    public void loadProcess(String processPath) {
        // 读取该流程的json文件(Action列表)

        addActionMeta("", "actionId", "Device", "deviceId");
    }

    public void executeProcess(String processId) {
        List<ActionMeta> actionMetaList = getActionMetaList(processId);

//        每个Action执行execute
        actionMetaList.forEach(actionMeta -> {
            Action action = null;
            try {
                action = actionBusiness.getAction(actionMeta.getType(), actionMeta.getObjectId(), actionMeta.getExecParam());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (action != null) {
                actionBusiness.executeAction(action, actionMeta.getExecParam());
            }
        });
    }

    public void addActionMeta(String processId, String actionId, String type, String objectId){

    }

    public List<ActionMeta> getActionMetaList(String processId) {
        ActionMeta actionMeta = new ActionMeta("actionId", "Device", "deviceId", "makeCoffee", "");
        ActionMeta actionMeta2 = new ActionMeta("actionId2", "Device", "deviceId", "check", "");
        return new ArrayList<>(Arrays.asList(actionMeta, actionMeta2));
    }
}
