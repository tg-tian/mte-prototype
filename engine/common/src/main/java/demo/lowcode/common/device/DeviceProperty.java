package demo.lowcode.common.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceProperty {
    private String identifier;
    private String name;
    private String accessMode;
    private boolean enableValidate;
    private List<ValidateParam> validateParams;
    private DeviceDataType deviceDataType;
}
