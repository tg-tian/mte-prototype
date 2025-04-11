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

import java.util.Date;

@Entity
@Data
@TableName("domain")
@Component
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "领域对象", description = "领域详细信息")
public class Domain {

    @Id
    @TableId(type = IdType.AUTO) // 明确指定主键类型（如自增）
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "领域类型编号", example = "1")
    private long domainId;

    @Column(name = "domainCode", nullable = false)
    @ApiModelProperty(value = "领域编码", example = "SmartBuilding")
    private String domainCode;

    @Column(name = "domainName", nullable = false)
    @ApiModelProperty(value = "领域名称", example = "智慧楼宇")
    private String domainName;

    @Column(name = "domainDescription", nullable = true)
    @ApiModelProperty(value = "领域描述", example = "this is a SmartBuilding")
    private String domainDescription;

    @Column(name = "status", nullable = false)
    @ApiModelProperty(value = "领域状态", example = "1")
    private String status;

    @Column(name = "createTime", nullable = false)
    @ApiModelProperty(value = "创建时间", example = "智慧楼宇")
    private Date createTime;

    @Column(name = "updateTime", nullable = false)
    @ApiModelProperty(value = "更新时间", example = "智慧楼宇")
    private Date updateTime;
}
