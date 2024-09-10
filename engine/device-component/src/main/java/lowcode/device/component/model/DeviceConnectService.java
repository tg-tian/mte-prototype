package lowcode.device.component.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceConnectService {
    private String name;
    private String uri;
    private Map<String, String> operations;
}
