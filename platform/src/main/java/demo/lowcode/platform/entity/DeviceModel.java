package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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

import java.time.LocalDateTime;

@Entity
@TableName(value = "device_model", autoResultMap = true)
@Data
@ApiModel(value = "设备模型对象", description = "设备模型定义的详细信息")
public class DeviceModel {
  @Id
  @TableId(type = IdType.AUTO)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "自增主键")
  private Long id;

  @Column(name = "modelId")
  @TableField("modelId")
  @ApiModelProperty(value = "模型编号")
  private String modelId;

  @Column(name = "modelName")
  @TableField("modelName")
  @ApiModelProperty(value = "模型名称")
  private String modelName;

  @Column(name = "provider")
  @ApiModelProperty(value = "供应商")
  private String provider;

  @Column(name = "category")
  @ApiModelProperty(value = "品类")
  private String category;

  @Column(name = "create_time")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createTime;

  @Column(name = "update_time")
  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updateTime;

  @TableField(value = "model", typeHandler = JacksonTypeHandler.class)
  @ApiModelProperty(value = "模型协议定义")
  private BaseDeviceModel model;
}


