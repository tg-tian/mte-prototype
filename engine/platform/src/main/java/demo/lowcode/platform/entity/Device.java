package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Resource
@TableName("device")
@ApiModel(value = "设备实例类",description = "属于某个设备类型的真实设备实例")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "设备实例编号", example = "1")
    private long deviceId;

    @Column(name = "deviceCode", nullable = false)
    @ApiModelProperty(value = "设备实例代码",example = "CoffeeMakerA")
    private String deviceCode;

    @Column(name = "deviceName", nullable = false)
    @ApiModelProperty(value = "设备实例名称",example = "咖啡机A")
    private String deviceName;

    @Column(name = "deviceTypeId")
    @ApiModelProperty(value = "设备类型编号",example = "1")
    private long deviceTypeId;

    @ManyToOne  // 多个 device 可以对应一个 DeviceType
    @JoinColumn(name = "deviceTypeId", referencedColumnName = "deviceTypeId", insertable = false)
    private DeviceType deviceType;  // 引用到 DeviceType 实体

    @Column(name = "versionNumber", nullable = false)
    @ApiModelProperty(value = "设备版本号",example = "1.0.0")
    private String versionNumber;

    @Column(name = "manufacturer", nullable = false)
    @ApiModelProperty(value = "厂商名称",example = "厂商A")
    private String manufacturer;
}
