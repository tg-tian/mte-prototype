package demo.lowcode.platform.liteflow;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import demo.lowcode.common.Action;
import demo.lowcode.common.ActionExecResult;
import demo.lowcode.platform.business.ActionBusiness;
import demo.lowcode.platform.model.ActionMeta;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@LiteflowComponent
@Service
public class ConferenceServiceCmp {
    @Resource
    ActionBusiness actionBusiness;

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "start", nodeName = "开始", nodeType = NodeTypeEnum.COMMON)
    public void processStart(NodeComponent bindCmp) {
        System.out.println("Start...");
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "makeCoffee", nodeName = "做咖啡", nodeType = NodeTypeEnum.COMMON)
    public void processMakeCoffee(NodeComponent bindCmp) {
        CustomContext context = bindCmp.getContextBean(CustomContext.class);
        String actionId = bindCmp.getNodeId();
        Action action = context.getActionMap().get(actionId);
        ActionMeta actionMeta = context.getActionMetaMap().get(actionId);

        Map<String, Map<String, Object>> executeActionArgs = bindCmp.getRequestData();

        context.getExecutionStatus().put(actionId, 2);

        System.out.println(executeActionArgs.get(actionId));

        ActionExecResult output = actionBusiness.executeAction(action, actionMeta.getExecParam(), executeActionArgs.get(actionId));
        context.getActionResults().put(actionId, output);

        context.getExecutionStatus().put(actionId, 3);
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "check", nodeName = "检查", nodeType = NodeTypeEnum.COMMON)
    public void processCheck(NodeComponent bindCmp) {
        CustomContext context = bindCmp.getContextBean(CustomContext.class);
        String actionId = bindCmp.getNodeId();
        Action action = context.getActionMap().get(actionId);
        ActionMeta actionMeta = context.getActionMetaMap().get(actionId);

        context.getExecutionStatus().put(actionId, 2);

        ActionExecResult output = actionBusiness.executeAction(action, actionMeta.getExecParam());
        context.getActionResults().put(actionId, output);

        context.getExecutionStatus().put(actionId, 3);
    }
}
