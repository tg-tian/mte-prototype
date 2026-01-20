package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import demo.lowcode.platform.model.devicev1.BaseDeviceModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@TableName(value = "devicetype_new", autoResultMap = true)
@Data
@ApiModel(value = "设备类型V1对象", description = "新版本设备类型定义的详细信息")
public class DeviceTypeV1 {
  @Id
  @TableId(type = IdType.AUTO)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "自增主键")
  private Long id;

  @Column(name = "modelName")
  @TableField("modelName")
  @ApiModelProperty(value = "模型名称")
  private String modelName;

  @Column(name = "provider", nullable = false)
  @ApiModelProperty(value = "供应商")
  private String provider;

  @Column(name = "category")
  @ApiModelProperty(value = "品类")
  private String category;

  @Column(name = "create_time")
  @TableField("create_time")
  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @Column(name = "update_time")
  @TableField("update_time")
  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  @TableField(value = "model", typeHandler = JacksonTypeHandler.class)
  @ApiModelProperty(value = "模型协议定义")
  private BaseDeviceModel model;
}
