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
import org.springframework.stereotype.Component;

@Entity
@Data
@TableName("domain_component")
@Component
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "领域-组件关系", description = "领域与组件的绑定关系")
public class DomainComponent {
    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "编号", example = "1")
    private Long id;

    @Column(name = "component_id", nullable = false)
    @ApiModelProperty(value = "绑定组件id", example = "SmartBuilding")
    private long componentId;

    @Column(name = "component_type", nullable = false)
    @ApiModelProperty(value = "绑定组件类型", example = "SmartBuilding")
    private String componentType;

    @Column(name = "domain_id", nullable = false)
    @ApiModelProperty(value = "绑定领域id", example = "SmartBuilding")
    private long domainId;

    @ManyToOne  // 多个组件可以对应一个领域
    @JoinColumn(name = "domainId", referencedColumnName = "domainId", insertable = false)
    private Domain domain;
}
