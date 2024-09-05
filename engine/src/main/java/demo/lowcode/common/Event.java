package demo.lowcode.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "事件功能实体抽象类，作为父类用于extend出各类具体事件，用于具体事件的实现")
public abstract class Event {
    @ApiModelProperty("事件名称")
    protected String message;

    @ApiModelProperty("事件反馈码")
    protected int code;
}
