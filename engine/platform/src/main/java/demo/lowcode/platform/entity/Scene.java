package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 场景类，描述场景的基本信息
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Resource
@TableName("scene")
@ApiModel(value = "场景实体类", description = "描述场景的详细信息")
public class Scene {

    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scene_id")
    @ApiModelProperty(value = "场景ID", example = "1")
    private Long sceneId;

    @Column(name = "scene_code")
    @ApiModelProperty(value = "场景代码", example = "The second interdisciplinary building")
    private String sceneCode;

    @Column(name = "scene_name", nullable = false)
    @ApiModelProperty(value = "场景名称", example = "第二学科交叉楼")
    private String sceneName;

    @Column(name = "scene_description")
    @ApiModelProperty(value = "场景描述", example = "This is the second interdisciplinary building")
    private String sceneDescription;

    @Column(name = "domain_id", nullable = false)
    @ApiModelProperty(value = "领域ID", example = "1")
    private long domainId;

    @Column(name = "status", nullable = false)
    @ApiModelProperty(value = "场景状态", example = "active")
    private String status;

    @Column(name = "longitude")
    @ApiModelProperty(value = "场景经度", example = "121.1")
    private Float longitude;

    @Column(name = "longitude")
    @ApiModelProperty(value = "场景纬度", example = "54.23")
    private Float latitude;

    @Column(name = "create_time", nullable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ManyToOne
    @TableField(exist = false)  //设置不管理数据库
    @JoinColumn(name = "domainId", referencedColumnName = "domainId", insertable = false, updatable = false)
    @ApiModelProperty(value = "关联的领域对象")
    private Domain domain;
}
