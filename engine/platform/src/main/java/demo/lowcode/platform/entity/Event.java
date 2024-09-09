package demo.lowcode.platform.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "事件信息实体类，用于存储事件的基本信息")
public class Event {
    @ApiModelProperty("事件名称")
    private String name;
    @ApiModelProperty("事件描述")
    private String description;
    @ApiModelProperty("事件类型")
    private String type;

}
