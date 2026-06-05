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
@TableName(value = "template", autoResultMap = true)
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "模板", description = "模板详细信息，与 lctemplates 外部模板库同步")
public class Template {
    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "本地主键", example = "1")
    private Long id;

    @Column(name = "template_id", nullable = false)
    @ApiModelProperty(value = "外部模板库中的 id")
    private Long template_id;

    @Column(name = "name")
    @ApiModelProperty(value = "模板名称")
    private String name;

    @Column(name = "template_index")
    @ApiModelProperty(value = "模板索引标识")
    private String template_index;

    @Column(name = "template_description")
    @ApiModelProperty(value = "模板描述")
    private String template_description;

    @Column(name = "example_image_url")
    @ApiModelProperty(value = "示例图片 URL")
    private String example_image_url;

    @Column(name = "code_url")
    @ApiModelProperty(value = "代码地址")
    private String code_url;

    @Column(name = "repository_url")
    @ApiModelProperty(value = "仓库地址")
    private String repository_url;

    @Column(name = "file_source")
    @ApiModelProperty(value = "文件来源")
    private String file_source;

    @Column(name = "submitter")
    @ApiModelProperty(value = "提交者")
    private String submitter;

    @Column(name = "license")
    @ApiModelProperty(value = "许可证")
    private String license;

    @Column(name = "code_file")
    @ApiModelProperty(value = "DSL 内容（JSON 字符串）")
    private String code_file;

    @Column(name = "tags")
    @ApiModelProperty(value = "标签 JSON（按 kind 分组）")
    private String tags;

    @Column(name = "created_at")
    @ApiModelProperty(value = "外部创建时间")
    private Date created_at;

    @Column(name = "updated_at")
    @ApiModelProperty(value = "外部更新时间")
    private Date updated_at;
}