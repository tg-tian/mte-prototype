package demo.lowcode.engine.business;

import demo.lowcode.common.Action;
import demo.lowcode.common.ActionExecResult;
import demo.lowcode.engine.model.ActionMeta;
import demo.lowcode.engine.model.RTProcess;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

import static demo.lowcode.engine.util.JsonUtils.evaluateCondition;

@Service
public class ProcessBusiness {
    @Resource
    ActionBusiness actionBusiness;

    public Map<String, Action> getProcessActions(String processId) {
        List<ActionMeta> actionMetaList = getActionMetaList(processId);
        Map<String, Action> actions = new HashMap<>();

        actionMetaList.forEach(actionMeta -> {
            try {
                Action action = actionBusiness.getAction(actionMeta.getType(), actionMeta.getObjectId(), actionMeta.getExecParam());
                actions.put(actionMeta.getActionId(), action);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return actions;
    }

    // 运行时
    public void executeProcess(String processId) {
        List<ActionMeta> actionMetaList = getActionMetaList(processId);
        Map<String, Action> actions = getProcessActions(processId);
        Map<String, Integer> executionStatus = new HashMap<>(); // 用于跟踪每个节点的执行状态
        // 初始化每个节点的执行状态
        actionMetaList.forEach(actionMeta -> executionStatus.put(actionMeta.getActionId(), 1));

        // 这里应该是根据processId获取到RTProcess
        RTProcess rtProcess = new RTProcess(processId, 1, executionStatus, null);
        rtProcess.setProcessStatus(2);
        rtProcess.setStartTime(new Date());

        Map<String, ActionExecResult> actionResults = new HashMap<>();

//        每个Action执行execute
        actionMetaList.forEach(actionMeta -> {
            if (executionStatus.get(actionMeta.getActionId()) == 4) {
                System.out.println("父节点中止，无需继续执行节点"+actionMeta.getActionId());
                return;
            }

            System.out.println("执行"+actionMeta.getActionId()+"...");
            executionStatus.put(actionMeta.getActionId(), 2);

            // 检查条件
            String condition = actionMeta.getCondition();
            boolean shouldExecute = true;
            if (condition.contains("${parent.code}")) {
                String parentActionId = actionMeta.getParentActionId();
                ActionExecResult parentOutput = actionResults.get(parentActionId);
                condition = condition.replace("${parent.code}", parentOutput != null ? parentOutput.getCode()+"" : "");
                shouldExecute = evaluateCondition(condition);
            }

            if (shouldExecute) {
                try {
                    Action action = actions.get(actionMeta.getActionId());

                    if (action != null) {
                        // 执行
                        ActionExecResult output;
                        String inputParam = actionMeta.getInputParam();
                        if (inputParam == null || inputParam.equals("")){
                            output = actionBusiness.executeAction(action, actionMeta.getExecParam());
                        }else {
                            // 获取父节点的输出并作为当前节点的输入参数
                            String parentActionId = actionMeta.getParentActionId();
                            ActionExecResult parentOutput = actionResults.get(parentActionId);
                            if (inputParam.contains("${parent.code}")) {
                                inputParam = inputParam.replace( "${parent.code}", parentOutput != null ? parentOutput.getCode()+"" : "");
                                System.out.println("父节点"+parentActionId+"的输出"+inputParam);
                                output = actionBusiness.executeAction(action, actionMeta.getExecParam(), Integer.parseInt(inputParam));
                            } else if (inputParam.contains("${parent.outputParam}")) {
                                inputParam = inputParam.replace( "${parent.outputParam}", parentOutput != null ? parentOutput.getOutputParams().toString() : "");
                                System.out.println("父节点"+parentActionId+"的输出"+inputParam);
                                assert parentOutput != null;
                                output = actionBusiness.executeAction(action, actionMeta.getExecParam(), parentOutput.getOutputParams());
                            } else {
                                throw new RuntimeException("Invalid InputParams");
                            }
                        }
                        actionResults.put(actionMeta.getActionId(), output);
                        executionStatus.put(actionMeta.getActionId(), 3);
                    }else {
                        throw new RuntimeException("无法获取action"+actionMeta.getActionName()+"对应的组件/构件"+actionMeta.getObjectId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("不满足条件:"+actionMeta.getCondition()+", 中止");
                setChildActionsStatus(actionMeta, actionMetaList, executionStatus, 4);
            }
        });
    }

    private void setChildActionsStatus(ActionMeta parentActionMeta, List<ActionMeta> actionMetaList,Map<String, Integer> executionStatus, int status){
        actionMetaList.stream()
                .filter(actionMeta -> parentActionMeta.getActionId().equals(actionMeta.getParentActionId()))
                .forEach(actionMeta -> {
                    executionStatus.put(actionMeta.getActionId(), status);
                    setChildActionsStatus(actionMeta, actionMetaList, executionStatus, status); // 递归设置子节点状态
                });
    }

    public void addActionMeta(String processId, String actionId, String type, String objectId){

    }

    public List<ActionMeta> getActionMetaList(String processId) {
        if (Objects.equals(processId, "ConferenceService")){
            ActionMeta actionMeta = new ActionMeta("start", "开始", "Default", "", "", "", null, "");
            ActionMeta actionMeta1 = new ActionMeta("makeCoffee", "制作咖啡", "Device", "start", "CoffeeMaker", "makeCoffee", null, "");
            ActionMeta actionMeta2 = new ActionMeta("check", "检查", "Device", "makeCoffee", "CoffeeMaker", "check", "${parent.code}", "${parent.code} == 0");
            return new ArrayList<>(Arrays.asList(actionMeta, actionMeta1, actionMeta2));
        }
        return new ArrayList<>();
    }
}
