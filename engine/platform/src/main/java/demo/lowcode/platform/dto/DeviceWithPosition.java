package demo.lowcode.platform.dto;

import demo.lowcode.platform.entity.Device;
import lombok.Data;

@Data
public class DeviceWithPosition extends Device {
    private String position;
    private boolean intelligent;
}
