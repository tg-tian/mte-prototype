package demo.lowcode.platform.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class ScenePubInfo {
    private Long sceneId;
    private String status;
    private String url;
    @Nullable
    private SceneTemInfo dslData;
}
