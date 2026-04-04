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
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domain_id")
    @ApiModelProperty(value = "领域类型编号", example = "1")
    private Long domainId;

    @Column(name = "domain_code", nullable = false)
    @ApiModelProperty(value = "领域编码", example = "SmartBuilding")
    private String domainCode;

    @Column(name = "domain_name", nullable = false)
    @ApiModelProperty(value = "领域名称", example = "智慧楼宇")
    private String domainName;

    @Column(name = "domain_description", nullable = true)
    @ApiModelProperty(value = "领域描述", example = "this is a SmartBuilding")
    private String domainDescription;

    @Column(name = "status", nullable = false)
    @ApiModelProperty(value = "领域状态", example = "1")
    private String status;

    @Column(name = "create_time", nullable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Column(name = "code_editor")
    @ApiModelProperty(value = "代码编辑器")
    private String codeEditor;

    @Column(name = "model_editor")
    @ApiModelProperty(value = "模型编辑器")
    private String modelEditor;

    @Column(name = "framework")
    @ApiModelProperty(value = "基础运行框架")
    private String framework;

    @Column(name = "dsl")
    @ApiModelProperty(value = "DSL标准")
    private String dsl;

    @Column(name = "url")
    @ApiModelProperty(value = "发布url")
    private String url;


}
