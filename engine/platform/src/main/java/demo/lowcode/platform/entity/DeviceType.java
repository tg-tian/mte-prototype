package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 设备类型实体类，用于存储设备类型用以与数据库交互的具体内容
 */

//实现JPA注释
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
    private long deviceTypeId;
    @Column(name = "deviceTypeCode", nullable = false)
    @ApiModelProperty(value = "设备类型代码", example = "CoffeeMaker")
    private String deviceTypeCode;
    @Column(name = "deviceTypeName", nullable = false)
    @ApiModelProperty(value = "设备类型名称", example = "咖啡机")
    private String deviceTypeName;
    @Column(name = "imgPath")
    @ApiModelProperty(value = "图标路径", example = "/images/smartphone.png")
    private String imgPath;
    @Column(name = "isPublish")
    @ApiModelProperty(value = "发布状态", example = "1")
    private boolean isPublish;
}
