package demo.lowcode.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMeta {
    private String deviceId;
    private String deviceName;
    private String scenarioId;
    private DeviceInformation mainObject;
}
