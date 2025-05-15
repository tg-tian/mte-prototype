package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("component")
public class Component {

    @TableId(value = "component_id", type = IdType.AUTO)
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
