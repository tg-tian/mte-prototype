package demo.lowcode.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ApplicationMeta {
    private String applicationId;
    private String applicationName;
    private String scenarioId;
    private List<ProcessMeta> processes;

}
