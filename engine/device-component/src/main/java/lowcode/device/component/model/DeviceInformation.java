package lowcode.device.component.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "设备信息类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInformation {
    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("设备类型")
    private String type;

    @ApiModelProperty("设备品牌")
    private DeviceConnectService service;

    // TODO： 设备其他信息，如接入类型、ip、端口号等
}
