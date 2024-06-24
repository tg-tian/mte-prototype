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
            }else {
                System.out.println("无法获取action"+actionMeta.getActionName()+"对应的component："+actionMeta.getObjectId());
            }
        });
    }

    public void addActionMeta(String processId, String actionId, String type, String objectId){

    }

    public List<ActionMeta> getActionMetaList(String processId) {
        if (Objects.equals(processId, "ConferenceService")){
            ActionMeta actionMeta = new ActionMeta("start", "开始", "Default", "", "", "", null, null, "");
            ActionMeta actionMeta1 = new ActionMeta("makeCoffee", "制作咖啡", "Device", "start", "CoffeeMaker", "makeCoffee", null, null, "");
            ActionMeta actionMeta2 = new ActionMeta("check", "检查", "Device", "makeCoffee", "CoffeeMaker", "check", null, null, "${parent.outputParam} == 1");
            return new ArrayList<>(Arrays.asList(actionMeta, actionMeta1, actionMeta2));
        }
        return new ArrayList<>();
    }
}
