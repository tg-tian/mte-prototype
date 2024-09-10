package demo.lowcode.platform.liteflow;

import demo.lowcode.common.Action;
import demo.lowcode.common.ActionExecResult;
import demo.lowcode.platform.model.ActionMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomContext {
    Map<String, Action> actionMap;
    Map<String, ActionMeta> actionMetaMap;
    Map<String, Integer> executionStatus; // 每个节点执行后的状态
    Map<String, ActionExecResult> actionResults;
}
