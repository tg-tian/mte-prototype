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
@TableName(value = "template", autoResultMap = true)
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "模板", description = "模板的详细信息，与模板库同步")
public class Template {
    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", example = "1")
    private Long id;

    @Column(name = "template_id", nullable = false)
    @ApiModelProperty(value = "模板库中的id")
    private Long template_id;

    @Column(name = "name")
    @ApiModelProperty(value = "模板名称")
    private String name;

    @Column(name = "description")
    @ApiModelProperty(value = "模板描述")
    private String description;

    @Column(name = "category")
    @ApiModelProperty(value = "模板类别")
    private String category;

    @Column(name = "tags")
    @ApiModelProperty(value = "模板标签")
    private String tags;

    @Column(name = "domain")
    @ApiModelProperty(value = "业务标签")
    private String domain;

    @Column(name = "image_url")
    @ApiModelProperty(value = "图片地址")
    private String image_url;

    @Column(name = "describing_the_model")
    @ApiModelProperty(value = "DSL/平台")
    private String describing_the_model;

    @Column(name = "url")
    @ApiModelProperty(value = "描述详情")
    private String url;
}
