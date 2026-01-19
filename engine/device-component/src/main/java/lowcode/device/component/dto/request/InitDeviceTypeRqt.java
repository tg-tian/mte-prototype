package lowcode.device.component.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitDeviceTypeRqt {
    public String deviceCode;
    public String deviceName;
    public String imageUrl;
}
