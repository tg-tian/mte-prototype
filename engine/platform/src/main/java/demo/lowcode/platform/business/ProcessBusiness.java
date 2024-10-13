package demo.lowcode.platform.business;

import demo.lowcode.common.Action;
import demo.lowcode.common.ActionExecResult;
import demo.lowcode.common.CommonConfig;
import demo.lowcode.common.Param;
import demo.lowcode.common.util.JavaDynamicCompiler;
import demo.lowcode.common.util.JsonUtils;
import demo.lowcode.platform.model.ActionMeta;
import demo.lowcode.platform.model.RTProcess;
import jakarta.annotation.Resource;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
        String scenarioId = "BuildingA";
        String applicationId = "GuestReception";
        String scePath = CommonConfig.getWorkspacePath()+domainId+"/"+scenarioId+"/";
        String procPath = scePath+"application/"+applicationId+"/process/";

        // 获取process对应json文件中的action节点信息
        List<ActionMeta> actionMetaList = getActionMetaList(processId, procPath);

        // 根据节点信息获取对应的action节点
        Map<String, Action> actionMap = getProcessActions(scenarioId, scePath, actionMetaList);

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
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);
            // TODO: 读取json并赋值
            Param param = new Param("coffeeType", "咖啡类型", "String");
            ActionMeta actionMeta = new ActionMeta("makeCoffee", "制作咖啡", "Device", "", "CoffeeMaker", "makeCoffee", new ArrayList<>(List.of(param)));
            ActionMeta checkMeta = new ActionMeta("check", "检查", "Device", "makeCoffee", "CoffeeMaker", "check", null);
            result.add(actionMeta);
            result.add(checkMeta);
        }catch (JSONException e){
            throw new RuntimeException("读取process信息失败："+e.getMessage());
        }
        return result;
    }

    private Map<String, Action> getProcessActions(String scenarioId, String scePath, List<ActionMeta> actionMetaList) {
        Map<String, Action> result = new HashMap<>();
        actionMetaList.forEach((actionMeta -> {
            Action action = null;
            try {
                action = actionBusiness.getAction(scenarioId, scePath, actionMeta);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            result.put(actionMeta.getActionId(), action);
        }));
        return result;
    }
}
