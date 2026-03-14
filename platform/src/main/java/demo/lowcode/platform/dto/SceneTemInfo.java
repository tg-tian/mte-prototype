package demo.lowcode.platform.dto;

import demo.lowcode.platform.entity.Device;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class SceneTemInfo {
    private NewScene sceneData;
    private List<NewDevice> devices;
}
