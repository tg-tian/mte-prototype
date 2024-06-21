package demo.lowcode.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInformation {
    private String deviceId;
    private Map<String, Object> propertyMap;
    private String serviceType;
    private String deviceType;
    private Map<String, String> eventMap;
    private String eventPath;
}
