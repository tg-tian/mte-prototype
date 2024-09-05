package demo.lowcode.engine.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@ApiModel(value = "参数类，用于存储各类操作的 输入参数 和 输出参数 的组成")
public class Param {
    @ApiModelProperty("参数代码")
    private String code;
    @ApiModelProperty("参数名称")
    private String name;
    @ApiModelProperty("参数类型")
    private String type;
    @ApiModelProperty("参数选择")
    private List<String> optional;

    public Param(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public Param(){
        this.code = "";
        this.name = "";
        this.type = "";
        this.optional =  new ArrayList<>();
    }

    public void Change(String code, String name, String type, List<String> optional){
        this.code = code;
        this.name = name;
        this.type = type;
        this.optional = optional;
    }

}
