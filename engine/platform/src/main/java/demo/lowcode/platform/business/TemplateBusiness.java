package demo.lowcode.platform.business;

import demo.lowcode.platform.dto.NewTemplate;
import demo.lowcode.platform.entity.Template;
import demo.lowcode.platform.mapper.TemplateMapper;

import java.util.List;

public class TemplateBusiness {
    private final TemplateMapper templateMapper;

    public TemplateBusiness(TemplateMapper templateMapper) {
        this.templateMapper = templateMapper;
    }

    public List<Template> getTemplateList(Long domainId) {
        List<Template> templateList = null;
        return templateList;
    }

    public void bindDomainAndTemplate(Long domainId, List<NewTemplate> templates) {
    }

    public void unbindDomainAndTemplate(Long domainId, Long templateId) {
    }
}
