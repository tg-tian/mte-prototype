package lowcode.device.component.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor

@ApiModel(value = "设备元数据类")
public class DeviceMeta {
    private String deviceId;
    private String deviceName;
    private String scenarioId;

    @ApiModelProperty("设备信息类") //设备信息类定义于DeviceInformation.java
    private DeviceInformation mainObject;
}
