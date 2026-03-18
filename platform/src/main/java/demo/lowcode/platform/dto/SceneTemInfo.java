package demo.lowcode.platform.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class SceneTemInfo {
    private NewScene sceneData;
    private List<Map<String, Object>> devices;
}
