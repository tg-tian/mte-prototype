package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import demo.lowcode.platform.model.device.ProtocolConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Resource
@TableName(value="device", autoResultMap = true)
@ApiModel(value = "设备实例类",description = "属于某个设备类型的真实设备实例")
public class Device {
    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "设备实例编号", example = "1")
    private Long id;

    @Column(name = "device_code", nullable = false)
    @ApiModelProperty(value = "设备实例代码",example = "CoffeeMakerA")
    private String deviceCode;

    @Column(name = "device_name", nullable = false)
    @ApiModelProperty(value = "设备实例名称",example = "咖啡机A")
    private String deviceName;

    @Column(name = "device_type_id")
    @ApiModelProperty(value = "设备类型编号",example = "1")
    private Long deviceTypeId;

    @ManyToOne  // 多个 device 可以对应一个 DeviceType
    @TableField(exist = false)  //设置不管理数据库
    @JoinColumn(name = "device_type_id", referencedColumnName = "device_type_id", insertable = false)
    private DeviceType deviceType;  // 引用到 DeviceType 实体

    @Column(name = "scene_id")
    @ApiModelProperty(value = "所在场景编号",example = "1")
    private Long sceneId;

    @ManyToOne  // 多个 device 可以对应一个 Scene
    @TableField(exist = false)  //设置不管理数据库
    @JoinColumn(name = "scene_id", referencedColumnName = "scene_id", insertable = false)
    private Scene scene;

    @Column(name = "status")
    @ApiModelProperty(value = "设备状态",example = "1")
    private int status;

    @Column(name = "protocol_type")
    @ApiModelProperty(value = "协议类型",example = "HTTP")
    private String protocolType;

    @ApiModelProperty(value = "协议配置",example = "HTTP")
    @TableField(value = "protocol_config", typeHandler = JacksonTypeHandler.class)
    private ProtocolConfig protocolConfig;

    @Column(name = "create_time", nullable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Column(name = "last_online_time", nullable = false)
    @ApiModelProperty(value = "上次在线时间")
    private Date lastOnlineTime;

    @Column(name = "device_location", nullable = false)
    @ApiModelProperty(value = "设备位置")
    private String deviceLocation;

    @Column(name = "device_position", nullable = false)
    @ApiModelProperty(value = "设备坐标")
    private String devicePosition;

    @Column(name = "intelligent", nullable = false)
    @ApiModelProperty(value = "智能化")
    private boolean intelligent;

}
