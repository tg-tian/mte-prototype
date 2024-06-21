package demo.lowcode.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessMeta {
    private String processId;
    private String scenarioId;
    private List<String> actionId;
}
