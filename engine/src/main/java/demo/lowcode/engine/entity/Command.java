package demo.lowcode.engine.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    private String code;
    @ApiModelProperty("操作名称")
    private String name;
    @ApiModelProperty("已绑定事件")
    private List<Event> events;

    public Command(String code, String name){
        this.code = code;
        this.name = name;
        this.events = new ArrayList<>();
    }
}
