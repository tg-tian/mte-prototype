package demo.lowcode.engine.business;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import demo.lowcode.common.Action;
import demo.lowcode.common.ActionExecResult;
import demo.lowcode.common.device.Device;
import demo.lowcode.common.device.DeviceService;
import demo.lowcode.common.Param;
import demo.lowcode.engine.liteflow.CustomContext;
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
    @Resource
    private FlowExecutor flowExecutor;

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

    public void executeLiteFlow(String processId, Map<String, Map<String, Object>> executeActionArgs) {
        List<ActionMeta> actionMetaList = getActionMetaList(processId);
        Map<String, Action> actions = getProcessActions(processId);
        Map<String, Integer> executionStatus = new HashMap<>(); // 用于跟踪每个节点的执行状态
        // 初始化每个节点的执行状态
        Map<String, ActionMeta> actionMetaMap = new HashMap<>();
        actionMetaList.forEach(actionMeta -> {
            executionStatus.put(actionMeta.getActionId(), 1);
            actionMetaMap.put(actionMeta.getActionId(), actionMeta);
        });
        Map<String, ActionExecResult> actionResults = new HashMap<>();

        CustomContext customContext = new CustomContext();
        customContext.setActionMap(actions);
        customContext.setActionMetaMap(actionMetaMap);
        customContext.setExecutionStatus(executionStatus);
        customContext.setActionResults(actionResults);
        LiteflowResponse response = flowExecutor.execute2Resp("ConferenceService", new HashMap<>(){{
            put("makeCoffee", new HashMap<>() {{
                put("coffeeType", "美式");
            }});
        }}, customContext);
    }

    // 运行时
    // TODO: 当有多个应用时，不同应用的功能执行应该可以并发/并行。executeProcess是否应该是 线程级 的？
    public void executeProcess(String processId, Map<String, Map<String, Object>> executeActionArgs) {
        System.out.println(executeActionArgs.toString());
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

            // 获取执行参数
            Map<String, Object> executeArgs = executeActionArgs.get(actionMeta.getActionId());

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
                            if (executeArgs == null || executeArgs.isEmpty()){
                                output = actionBusiness.executeAction(action, actionMeta.getExecParam());
                            } else {
                                output = actionBusiness.executeAction(action, actionMeta.getExecParam(), executeArgs);
                            }
                        }else {
                            // 获取父节点的输出并作为当前节点的输入参数
                            String parentActionId = actionMeta.getParentActionId();
                            ActionExecResult parentOutput = actionResults.get(parentActionId);
                            if (inputParam.contains("${parent.code}")) {
                                inputParam = inputParam.replace( "${parent.code}", parentOutput != null ? parentOutput.getCode()+"" : "");
//                                System.out.println("父节点"+parentActionId+"的输出"+inputParam);
                                output = actionBusiness.executeAction(action, actionMeta.getExecParam(), Integer.parseInt(inputParam));
                            } else if (inputParam.contains("${parent.outputParam}")) {
                                inputParam = inputParam.replace( "${parent.outputParam}", parentOutput != null ? parentOutput.getOutputParams().toString() : "");
//                                System.out.println("父节点"+parentActionId+"的输出"+inputParam);
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

    // TODO: read from CurrentApp
    public List<ActionMeta> getActionMetaList(String processId) {
        if (Objects.equals(processId, "ConferenceService")){
            ActionMeta actionMeta = new ActionMeta("start", "开始", "Default", "", "", "", null, "");
            ActionMeta actionMeta1 = new ActionMeta("makeCoffee", "制作咖啡", "Device", "start", "CoffeeMaker", "makeCoffee", null, "");
            ActionMeta actionMeta2 = new ActionMeta("check", "检查", "Device", "makeCoffee", "CoffeeMaker", "check", null, "${parent.code} == 0");
            return new ArrayList<>(Arrays.asList(actionMeta, actionMeta1, actionMeta2));
        }
        return new ArrayList<>();
    }

    public Map<String, List<Param>> getProcessConfig(String processId) {
        List<ActionMeta> actionMetaList = getActionMetaList(processId);
        Map<String, Action> actionMap = getProcessActions(processId);

        Map<String, List<Param>> result = new HashMap<>();

        for (Map.Entry<String, Action> entry : actionMap.entrySet()) {
            Optional<ActionMeta> actionMetaFind = actionMetaList.stream().filter(meta -> meta.getActionId().equals(entry.getKey())).findFirst();
            actionMetaFind.ifPresent(actionMeta -> {
                if (entry.getValue() instanceof Device) {
                    Device device = (Device) entry.getValue();
                    DeviceService service = device.getDeviceService();
                    Map<String, Object> serviceProperty = service.getProperty();
                    List<Param> params = new ArrayList<>();

                    // 读取当前节点操作
                    String operation = actionMeta.getExecParam();

                    // 读取该操作需要的inputParam
                    if (Objects.equals(operation, "makeCoffee")) {
                        // mock
                        Param param = new Param("coffeeType", "咖啡类型", "Enum");

                        // 若param类型为enum，则读取serviceProperty
                        if (Objects.equals(param.getType(), "Enum")) {
                            param.setOptional((List<Object>) serviceProperty.get(param.getCode()));
                        }
                        params.add(param);
                    }

                    result.put(entry.getKey(), params);
                }
            });
        }

        return result;
    }
}
