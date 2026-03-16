package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import demo.lowcode.platform.common.Action;
import demo.lowcode.platform.common.ActionExecResult;
import demo.lowcode.platform.common.CommonConfig;
import demo.lowcode.platform.common.Param;
import demo.lowcode.platform.common.util.JsonUtils;
import demo.lowcode.platform.model.ActionMeta;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
                // TODO
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

        Map<String, List<Param>> result = new HashMap<>();
        // TODO:读取每个action节点参数

        return result;
    }
}
