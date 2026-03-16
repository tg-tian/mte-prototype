package demo.lowcode.platform.business;

import demo.lowcode.platform.common.Action;
import demo.lowcode.platform.common.ActionExecResult;
import demo.lowcode.platform.model.ActionMeta;
import jakarta.annotation.Resource;

import java.util.Map;

@org.springframework.stereotype.Service
public class ActionBusiness {
    @Resource
    SceneBusiness sceneBusiness;

    public Action getAction(String scenarioId, String scePath, ActionMeta actionMeta, Map<String, Object> executeArgs) throws Exception {
        return new Action() {
            @Override
            public ActionExecResult execute(Object... args) {
                return null;
            }
        };
    }

    public ActionExecResult executeAction(Action action, Object... args) {
        System.out.println("----------开始执行----------");
        ActionExecResult result = action.execute(args);
        if (result == null || result.getCode() == 0){
        }else {
        }
        System.out.println("----------结束执行----------");
        return result;
    }
}
