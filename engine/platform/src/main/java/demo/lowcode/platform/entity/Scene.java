package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sceneId", nullable = false)
    @ApiModelProperty(value = "场景ID", example = "1")
    private long sceneId;

    @Column(name = "sceneCode", nullable = false)
    @ApiModelProperty(value = "场景代码", example = "The second interdisciplinary building")
    private String sceneCode;

    @Column(name = "sceneName", nullable = false)
    @ApiModelProperty(value = "场景名称", example = "第二学科交叉楼")
    private String sceneName;

    @Column(name = "sceneDescription")
    @ApiModelProperty(value = "场景描述", example = "This is the second interdisciplinary building")
    private String sceneDescription;

    @Column(name = "domainId", nullable = false)
    @ApiModelProperty(value = "领域ID", example = "1")
    private long domainId;

    @ManyToOne
    @JoinColumn(name = "domainId", referencedColumnName = "domainId", insertable = false, updatable = false)
    @ApiModelProperty(value = "关联的领域对象")
    private Domain domain;
}
