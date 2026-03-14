package demo.lowcode.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessMeta {
    private String processId;
    private String processName;
    private String applicationId;
    private String parentProcessId;
    private List<ActionMeta> actions;
}
