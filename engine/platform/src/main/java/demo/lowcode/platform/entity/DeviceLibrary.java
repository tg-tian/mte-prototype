package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("device_library")
@Entity
@ApiModel(value = "设备库", description = "设备库信息")
public class DeviceLibrary {
    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @Column(nullable = false)
    @ApiModelProperty(value = "设备厂商")
    private String provider;

    @Column(nullable = false)
    @ApiModelProperty(value = "设备分类")
    private String category;

    @Column(name = "device_model", nullable = false)
    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @Column(name = "device_name")
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @Column(name = "device_mapper_path")
    @ApiModelProperty(value = "设备Mapper路径")
    private String deviceMapperPath;
}
