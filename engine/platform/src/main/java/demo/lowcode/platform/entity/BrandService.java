package demo.lowcode.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备支持类")
public class BrandService {
    @ApiModelProperty("厂家名称")
    String name;
    @ApiModelProperty("支持服务描述")
    String description;

}
