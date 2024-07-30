package demo.lowcode.engine.model;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class ScenarioJson {
    private String scenarioId;
    private String scenarioName;
    private String domainId;
    private String mapPath;
    private List<Map<String,String>> maplist;
}
