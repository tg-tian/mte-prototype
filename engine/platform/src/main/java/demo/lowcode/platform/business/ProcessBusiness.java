package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import demo.lowcode.common.Action;
import demo.lowcode.common.ActionExecResult;
import demo.lowcode.common.CommonConfig;
import demo.lowcode.common.Param;
import demo.lowcode.common.device.Device;
import demo.lowcode.common.device.DeviceService;
import demo.lowcode.common.util.JavaDynamicCompiler;
import demo.lowcode.common.util.JsonUtils;
import demo.lowcode.platform.model.ActionMeta;
import demo.lowcode.platform.model.RTProcess;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.*;

@Service
public class ProcessBusiness {
    @Resource
    ActionBusiness actionBusiness;

    public void executeProcess(String processId, Map<String, Map<String, Object>> executeActionArgs){
        String domainId = "SmartBuilding";
        String scenarioId = "PhysicalBuilding";
        String applicationId = "SelfServeCoffee";
        String scePath = CommonConfig.getWorkspacePath()+domainId+"/"+scenarioId+"/";
        String procPath = scePath+"application/"+applicationId+"/process/";

        // 获取process对应json文件中的action节点信息
        List<ActionMeta> actionMetaList = getActionMetaList(processId, procPath);

        // 根据节点信息获取对应的action节点
        Map<String, Action> actionMap = getProcessActions(scenarioId, scePath, actionMetaList, executeActionArgs);

        Map<String, Integer> executionStatus = new HashMap<>(); // 跟踪每个Action的执行状态
        Map<String, ActionExecResult> actionResults = new HashMap<>(); // 记录每个Action的执行结果
        // 初始化每个节点的执行状态
        actionMetaList.forEach(actionMeta -> executionStatus.put(actionMeta.getActionId(), 1));

        // 这里应该是根据processId获取到RTProcess
        RTProcess rtProcess = new RTProcess(processId, 1, executionStatus, null);
        rtProcess.setProcessStatus(2);
        rtProcess.setStartTime(new Date());

        // TODO:每个Action执行execute,目前是依次执行
        actionMetaList.forEach(actionMeta -> {
            if (executionStatus.get(actionMeta.getActionId()) == 4) {
                System.out.println("父节点中止，无需继续执行节点"+actionMeta.getActionId());
                return;
            }

            // 获取执行参数
            Map<String, Object> executeArgs = executeActionArgs.get(actionMeta.getActionId());

            System.out.println("执行"+actionMeta.getActionId()+"...");
            executionStatus.put(actionMeta.getActionId(), 2);

            // 获取Action执行节点
            Action action = actionMap.get(actionMeta.getActionId());
            if (action != null){
                // 执行
                ActionExecResult output = null;
                if (executeArgs == null || executeArgs.isEmpty()){
                    output = actionBusiness.executeAction(action, actionMeta.getExecParam());
                } else {
                    try {
                        String jarPath = scePath+"device/"+actionMeta.getObjectId()+"/"+actionMeta.getObjectId().toLowerCase()+"-1.0.0.jar";
                        URLClassLoader classLoader = JavaDynamicCompiler.loadJar(jarPath);
                        Class<?> serviceClass = classLoader.loadClass("lowcode.device."+actionMeta.getObjectId().toLowerCase()+".service."+actionMeta.getObjectId()+"Service");
                        // 获取所有方法找到要调用的具体方法从而得到参数
                        Method[] methods = serviceClass.getMethods();
                        for (Method method : methods) {
                            if (method.getName().equals(actionMeta.getExecParam())){
                                Class<?>[] parameterTypes = method.getParameterTypes();
                                Object[] params = new Object[parameterTypes.length];

                                int index = 0;
                                for (Class<?> paramType : parameterTypes) {
                                    String paramName = method.getParameters()[index].getName();

                                    if (executeArgs.containsKey(paramName)) {
                                        // 将 Map 中的值转换为对应类型
                                        params[index] = paramType.cast(executeArgs.get(paramName));
                                    } else {
                                        throw new IllegalArgumentException("参数 " + paramName + " 缺失");
                                    }
                                    index++;
                                }
                                output = actionBusiness.executeAction(action, actionMeta.getExecParam(), params);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new RuntimeException(e.getMessage());
                    }
                }
                actionResults.put(actionMeta.getActionId(), output);
                executionStatus.put(actionMeta.getActionId(), 3);
            }else {
                throw new RuntimeException("无法获取action"+actionMeta.getActionName()+"对应的组件/构件"+actionMeta.getObjectId());
            }
        });
    }

    public List<ActionMeta> getActionMetaList(String processId, String procPath) {
        List<ActionMeta> result = new ArrayList<>();
        String filePath = procPath+processId+".proc";
        String jsonContent = JsonUtils.readJson(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            ArrayNode actionArray = (ArrayNode) jsonNode.get("actionList");
            for (int i=0;i<actionArray.size();i++){
                ObjectNode actionObject = (ObjectNode) actionArray.get(i);
                String actionId = actionObject.get("actionId").asText();
                String actionName = actionObject.get("actionName").asText();
                String type = actionObject.get("type").asText();
                String parentActionId = actionObject.get("parentActionId").asText();
                String objectId = actionObject.get("objectId").asText();
                String execParam = actionObject.get("execParam").asText();

                ArrayNode paramArray = (ArrayNode)actionObject.get("executeArgs");
                List<Param> params = new ArrayList<>();
                for (int j=0;j<paramArray.size();j++){
                    ObjectNode paramObject = (ObjectNode)paramArray.get(j);
                    String paramCode = paramObject.get("code").asText();
                    String paramName = paramObject.get("name").asText();
                    String paramType = paramObject.get("type").asText();
                    Param param = new Param(paramCode, paramName, paramType);
                    params.add(param);
                }

                ActionMeta actionMeta = new ActionMeta(actionId, actionName, type, parentActionId, objectId, execParam, params);
                result.add(actionMeta);
            }
//            Param param = new Param("coffeeType", "咖啡类型", "String");
//            ActionMeta actionMeta = new ActionMeta("makeCoffee", "制作咖啡", "Device", "", "CoffeeMaker", "makeCoffee", new ArrayList<>(List.of(param)));
//            ActionMeta checkMeta = new ActionMeta("check", "检查", "Device", "makeCoffee", "CoffeeMaker", "check", null);
//            result.add(actionMeta);
//            result.add(checkMeta);
        }catch (Exception e){
            throw new RuntimeException("读取process信息失败："+e.getMessage());
        }
        return result;
    }

    private Map<String, Action> getProcessActions(String scenarioId, String scePath, List<ActionMeta> actionMetaList, Map<String, Map<String, Object>> executeActionArgs) {
        Map<String, Action> result = new HashMap<>();
        actionMetaList.forEach((actionMeta -> {
            Action action = null;
            try {
                action = actionBusiness.getAction(scenarioId, scePath, actionMeta, executeActionArgs.get(actionMeta.getActionId()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            result.put(actionMeta.getActionId(), action);
        }));
        return result;
    }

    public Map<String, List<Param>> getProcessConfig(String processId) {
        String domainId = "SmartBuilding";
        String scenarioId = "PhysicalBuilding";
        String applicationId = "SelfServeCoffee";
        String scePath = CommonConfig.getWorkspacePath()+domainId+"/"+scenarioId+"/";
        String procPath = scePath+"application/"+applicationId+"/process/";

        List<ActionMeta> actionMetaList = getActionMetaList(processId, procPath);
//        Map<String, Action> actionMap = getProcessActions(scenarioId, scePath, actionMetaList, new HashMap<>());

        Map<String, List<Param>> result = new HashMap<>();
        // TODO:读取每个action节点参数

//        for (Map.Entry<String, Action> entry : actionMap.entrySet()) {
//            Optional<ActionMeta> actionMetaFind = actionMetaList.stream().filter(meta -> meta.getActionId().equals(entry.getKey())).findFirst();
//            actionMetaFind.ifPresent(actionMeta -> {
//                if (entry.getValue() instanceof Device) {
//                    Device device = (Device) entry.getValue();
//                    DeviceService service = device.getDeviceService();
//                    Map<String, Object> serviceProperty = service.getProperty();
//                    List<Param> params = new ArrayList<>();
//
//                    // 读取当前节点操作
//                    String operation = actionMeta.getExecParam();
//
//                    // 读取该操作需要的inputParam
//                    if (Objects.equals(operation, "makeCoffee")) {
//                        // mock
//                        Param param = new Param("coffeeType", "咖啡类型", "Enum");
//
//                        // 若param类型为enum，则读取serviceProperty
//                        if (Objects.equals(param.getType(), "Enum")) {
//                            param.setOptions((List<String>) serviceProperty.get(param.getCode()));
//                        }
//                        params.add(param);
//                    }
//
//                    result.put(entry.getKey(), params);
//                }
//            });
//        }

        return result;
    }
}
