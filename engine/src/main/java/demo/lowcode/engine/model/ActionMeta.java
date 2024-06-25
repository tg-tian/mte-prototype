package demo.lowcode.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

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
    private String inputParam;
    private String condition;
}
