package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import demo.lowcode.platform.model.device.Model;
import demo.lowcode.platform.util.JsonToModelConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Entity
@TableName("devicetype")
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备类型对象", description = "设备类型的详细信息")
public class DeviceType {
    @Id
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

    @Convert(converter = JsonToModelConverter.class)
    @Column(name = "model", columnDefinition = "json")
    @ApiModelProperty(value = "模型定义")
    private Model model;
}
