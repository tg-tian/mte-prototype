package demo.lowcode.platform.dto;

import lombok.Data;

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
    private String createTime;
    private String updateTime;
}
