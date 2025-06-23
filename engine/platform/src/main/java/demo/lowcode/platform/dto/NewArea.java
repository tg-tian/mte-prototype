package demo.lowcode.platform.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import demo.lowcode.platform.entity.Scene;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
public class NewArea {
    private Long id;
    private String name;
    private Long sceneId;
    private Scene scene; // 引用到 Scene 实体
    private String description;
    private String image;
    private String position;
    private Long parentId;
    private List<NewArea> children;
}
