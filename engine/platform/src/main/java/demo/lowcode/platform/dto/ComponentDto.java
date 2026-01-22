package demo.lowcode.platform.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ComponentDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String type; // 'node' or 'edge'
    private String purpose; // 'businessFlow', 'interfaceFlow', or 'deviceLogic'
    private ConstraintDto inputConstraint; // for node type
    private ConstraintDto outputConstraint; // for node type
    private ConstraintDto startConstraint; // for edge type
    private ConstraintDto endConstraint; // for edge type
    private Map<String, Object> properties; // 节点属性
    private List<Map<String, Object>> inputs; // 入口参数
    private List<Map<String, Object>> outputs; // 出口参数
    private String createTime;
    private String updateTime;
}
