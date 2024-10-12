package demo.lowcode.platform.entity;

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
@TableName("domain")
@Component
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "领域对象", description = "领域详细信息")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "领域类型编号", example = "1")
    private long domainId;

    @Column(name = "domainName", nullable = false)
    @ApiModelProperty(value = "领域名称", example = "SmartBuilding")
    private String domainName;

    @Column(name = "domainDescription", nullable = true)
    @ApiModelProperty(value = "领域描述", example = "this is a SmartBuilding")
    private String domainDescription;
}
