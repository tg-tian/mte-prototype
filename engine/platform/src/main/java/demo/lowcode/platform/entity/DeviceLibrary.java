package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "device_library1", autoResultMap = true)
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

    @Column(name = "device_type_id")
    @ApiModelProperty(value = "设备类型id")
    private Integer deviceTypeId;

    @Column(name = "device_type_name", nullable = false)
    @ApiModelProperty(value = "设备类型名称")
    private String deviceTypeName;

    @Column(name = "device_model", nullable = false)
    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @Column(name = "device_name")
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @Column(name = "device_mapper_path")
    @ApiModelProperty(value = "设备Mapper路径")
    private String deviceMapperPath;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Column(columnDefinition = "json")
    @ApiModelProperty(value = "属性映射 (设备属性 -> 设备类型属性)")
    private Map<String, String> propertyMap;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Column(columnDefinition = "json")
    @ApiModelProperty(value = "操作实现 (操作ID -> 自定义实现代码)")
    private Map<String, String> actionMap;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
