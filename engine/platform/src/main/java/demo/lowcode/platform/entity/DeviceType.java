package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import demo.lowcode.platform.model.device.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Entity
@TableName(value = "devicetype", autoResultMap = true)
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备类型对象", description = "设备类型的详细信息")
public class DeviceType {
    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "设备类型编号", example = "1")
    private Long id;

    @Column(name = "code", nullable = false)
    @ApiModelProperty(value = "设备类型代码", example = "CoffeeMaker")
    private String code;

    @Column(name = "name", nullable = false)
    @ApiModelProperty(value = "设备类型名称", example = "咖啡机")
    private String name;

    @Column(name = "description", nullable = false)
    @ApiModelProperty(value = "设备类型描述", example = "咖啡机")
    private String description;

    @Column(name = "create_time", nullable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

//    @Convert(converter = JsonToModelConverter.class)
//    @Column(name = "model", columnDefinition = "json")
    @TableField(value = "model", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "模型定义")
    private Model model;
}
