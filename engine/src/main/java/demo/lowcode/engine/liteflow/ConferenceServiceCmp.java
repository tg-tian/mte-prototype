package demo.lowcode.engine.liteflow;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;

@LiteflowComponent
public class ConferenceServiceCmp {
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "start", nodeName = "开始", nodeType = NodeTypeEnum.COMMON)
    public void processStart(NodeComponent bindCmp) {
        System.out.println("Start...");
    }
}
