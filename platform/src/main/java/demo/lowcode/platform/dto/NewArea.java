package demo.lowcode.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import demo.lowcode.platform.entity.Scene;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewArea {
    private Long id;
    private String name;
    private Long sceneId;
    private Scene scene; // 引用到 Scene 实体
    private String description;
    private String image;
    private String imageRef;
    private String polygon;
    private Long parentId;
    private List<NewArea> children;
}
