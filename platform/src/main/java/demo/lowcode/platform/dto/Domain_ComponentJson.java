package demo.lowcode.platform.dto;

import demo.lowcode.platform.entity.ComponentAbout;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class Domain_ComponentJson {
    private String componentType;
    private List<ComponentAbout> componentAbout;
}
