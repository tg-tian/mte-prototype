package demo.lowcode.platform.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
public class ScenarioMeta {
    private String scenarioId;
    private String scenarioName;
    private String domainId;
    private Map<String, String> map;
    private List<DeviceMeta> devices;
}
