package demo.lowcode.platform.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class SceneTemInfo {
    private NewScene sceneData;
    private DomainTemInfo domainInfo;
    private List<NewArea> areaTree;
}
