package lowcode.device.component.dto.request;

import demo.lowcode.common.device.DeviceProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPropertyRqt {
    private String deviceType;
    private List<DeviceProperty> properties;
}
