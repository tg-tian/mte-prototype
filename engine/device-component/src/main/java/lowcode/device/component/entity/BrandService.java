package lowcode.device.component.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
@Component
@NoArgsConstructor
@ApiModel(value = "设备支持类")
public class BrandService {
    @ApiModelProperty("厂家名称")
    String name;
    @ApiModelProperty("支持服务描述")
    String description;
    @ApiModelProperty("支持配置文件名称")
    String filename;
}
