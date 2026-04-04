package demo.lowcode.platform.business;

import demo.lowcode.platform.entity.Template;
import demo.lowcode.platform.mapper.DomainTemplateMapper;
import demo.lowcode.platform.mapper.TemplateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemplateBusiness {
    private final TemplateMapper templateMapper;
    private final DomainTemplateMapper domainTemplateMapper;

    public TemplateBusiness(TemplateMapper templateMapper, DomainTemplateMapper domainTemplateMapper) {
        this.templateMapper = templateMapper;
        this.domainTemplateMapper = domainTemplateMapper;
    }

    public List<Template> getAllTemplates() {
        return templateMapper.selectList(null);
    }

    @Transactional
    public void bindDomainAndTemplate(Long domainId, Long templateId) {
        if (domainId == null || templateId == null) {
            throw new RuntimeException("领域ID和模板ID不能为空");
        }
        Template template = templateMapper.selectById(templateId);
        if (template == null) {
            throw new RuntimeException("模板不存在");
        }
        domainTemplateMapper.insertDomainTemplateRelation(domainId, templateId);
    }

    @Transactional
    public void unbindDomainAndTemplate(Long domainId, Long templateId) {
        // 根据template_id获取模板
        Template template = templateMapper.selectById(templateId);
        if (template == null) {
            throw new RuntimeException("Template not found with template_id: " + templateId);
        }

        // 删除领域模板关系
        domainTemplateMapper.deleteDomainTemplateRelation(domainId, template.getId());
    }
}
