package demo.lowcode.common;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import demo.lowcode.common.Param;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.naming.StringManager;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "操作列表类")
public class Command {
    @ApiModelProperty("操作码")
    private String commandCode;
    @ApiModelProperty("操作名称")
    private String commandName;
    @ApiModelProperty("输入参数")
    private List<Param> inputParam;
    @ApiModelProperty("输出参数")
    private String outputParam;
    
    public Command(String commandCode,String commandName)
    {
        this.commandCode = commandCode;
        this.commandName = commandName;
    }
}
