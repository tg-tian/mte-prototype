package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("component")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "组件", description = "组件的详细信息")
public class Component {

    @Id
    @TableId(value = "component_id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", example = "1")
    private Long componentId;

    @TableField("component_code")
    private String componentCode;

    @TableField("component_name")
    private String componentName;

    @TableField("component_description")
    private String componentDescription;

    @TableField("component_type")
    private String componentType; // 'node' or 'edge'

    @TableField("component_purpose")
    private String componentPurpose; // 'businessFlow', 'interfaceFlow', or 'deviceLogic'

    @TableField("constraints")
    private String constraints; // JSON string to store constraints

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;
}
