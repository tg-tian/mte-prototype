package demo.lowcode.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_file")
@ApiModel(value = "文件存储实体", description = "存储上传到数据库中的文件")
public class StoredFile {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "文件ID")
    private Long id;

    @ApiModelProperty(value = "系统文件名")
    private String fileName;

    @ApiModelProperty(value = "原始文件名")
    private String originalName;

    @ApiModelProperty(value = "文件类型")
    private String contentType;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "文件二进制内容")
    private byte[] fileData;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "业务ID")
    private Long bizId;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;
}
