package demo.lowcode.platform.entity;

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

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "area", autoResultMap = true)
@ApiModel(value = "区域类", description = "场景区域")
public class Area {

    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "区域ID", example = "1")
    private Long id;

    @Column(name = "name", nullable = false)
    @ApiModelProperty(value = "区域名", example = "办公区")
    private String name;

    @Column(name = "scene_id", nullable = false)
    @ApiModelProperty(value = "关联场景ID", example = "1")
    private Long sceneId;

    @ManyToOne
    @TableField(exist = false) // 设置不管理数据库
    @JoinColumn(name = "scene_id", referencedColumnName = "scene_id", insertable = false, updatable = false)
    private Scene scene; // 引用到 Scene 实体

    @Column(name = "description")
    @ApiModelProperty(value = "区域描述", example = "这是一个办公区域")
    private String description;

    @ApiModelProperty(value = "区域布局图", example = "base64编码的图片数据")
    @TableField(value = "image")
    private String image;

    @ApiModelProperty(value = "区域坐标", example = "{\"x\":100,\"y\":200}")
    @TableField(value = "position")
    private String position;

    @ApiModelProperty(value = "父区域ID", example = "0")
    @TableField(value = "parent_id")
    private Long parentId;

}
