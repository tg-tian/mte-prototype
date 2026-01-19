package demo.lowcode.platform.business;

import demo.lowcode.platform.dto.NewTemplate;
import demo.lowcode.platform.entity.Template;
import demo.lowcode.platform.mapper.TemplateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemplateBusiness {
    private final TemplateMapper templateMapper;

    public TemplateBusiness(TemplateMapper templateMapper) {
        this.templateMapper = templateMapper;
    }

    public List<Template> getTemplateList(Long domainId) {
        return templateMapper.getDomainTemplate(domainId);
    }

    @Transactional
    public void bindDomainAndTemplate(Long domainId, List<NewTemplate> templates) {
        // 判断新模板是否在数据库中，若不存在则新增
        for (NewTemplate newTemplate : templates) {
            // 根据 template_id 检查模板是否已存在
            Template existingTemplate = templateMapper.selectByTemplateId(newTemplate.getId());

            if (existingTemplate == null) {
                // 创建新模板
                Template template = new Template();
                template.setTemplate_id(newTemplate.getId());
                template.setName(newTemplate.getName());
                template.setDescription(newTemplate.getDescription());
                template.setCategory(newTemplate.getCategory());
                template.setTags(newTemplate.getTags());
                template.setDomain(newTemplate.getDomain());
                template.setImage_url(newTemplate.getImage_url());
                template.setDescribing_the_model(newTemplate.getDescribing_the_model());
                template.setUrl(newTemplate.getUrl());

                templateMapper.insert(template);
                existingTemplate = template;
            }

            // 将新模板id与领域对应关系存入关系表中
            templateMapper.insertDomainTemplateRelation(domainId, existingTemplate.getId());
        }
    }

    @Transactional
    public void unbindDomainAndTemplate(Long domainId, Long templateId) {
        // 根据template_id获取模板
        Template template = templateMapper.selectById(templateId);
        if (template == null) {
            throw new RuntimeException("Template not found with template_id: " + templateId);
        }

        // 删除领域模板关系
        templateMapper.deleteDomainTemplateRelation(domainId, template.getId());
    }
}
