package lowcode.device.component.dto;

import demo.lowcode.common.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "操作DTO")
public class CommandDto {
    @ApiModelProperty("操作码")
    private String commandCode;
    @ApiModelProperty("操作名称")
    private String commandName;
    @ApiModelProperty("输入参数")
    private List<Param> inputParam;
    @ApiModelProperty("输出参数")
    private String outputParam;
    @ApiModelProperty("事件")
    private List<String> events;
    @ApiModelProperty("服务")
    private List<String> services;
}
