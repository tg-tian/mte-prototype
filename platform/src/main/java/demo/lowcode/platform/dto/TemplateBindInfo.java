package demo.lowcode.platform.dto;

import demo.lowcode.platform.entity.Template;
import lombok.Data;

import java.util.List;

@Data
public class TemplateBindInfo {
    private Long domainId;
    private List<NewTemplate> templates;
}
