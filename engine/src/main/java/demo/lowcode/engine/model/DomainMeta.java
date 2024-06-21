package demo.lowcode.engine.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class DomainMeta {
    private String domainId;
    private String domainName;
    private List<String> deviceType;
    private Map<String, List<String>> serviceType;
}
