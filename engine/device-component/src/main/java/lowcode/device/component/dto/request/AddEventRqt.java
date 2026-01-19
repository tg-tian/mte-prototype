package lowcode.device.component.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lowcode.device.component.entity.DeviceEvent;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddEventRqt {
    public String deviceType;
    public String commandCode;
    public DeviceEvent deviceEvent;
}
