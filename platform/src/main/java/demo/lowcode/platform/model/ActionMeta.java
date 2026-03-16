package demo.lowcode.platform.model;

import demo.lowcode.platform.common.Param;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionMeta {
    private String actionId;
    private String actionName;
    private String type; // Default/Device/Page/Service
    private String parentActionId;
    private String objectId;
    private String execParam;
    private List<Param> executeArgs;
}
