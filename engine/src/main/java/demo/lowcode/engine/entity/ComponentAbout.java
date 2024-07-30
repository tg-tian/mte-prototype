package demo.lowcode.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentAbout {
    private String componentId;
    private String componentName;
    private String imgPath;
    private String brief;
}
