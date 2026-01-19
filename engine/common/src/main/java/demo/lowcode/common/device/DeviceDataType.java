package demo.lowcode.common.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDataType {
    private String type;
    private Map<String, String> specs;
}
