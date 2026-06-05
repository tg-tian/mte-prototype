package demo.lowcode.platform.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.platform.entity.Template;
import demo.lowcode.platform.mapper.DomainTemplateMapper;
import demo.lowcode.platform.mapper.TemplateMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TemplateBusiness {
    private final TemplateMapper templateMapper;
    private final DomainTemplateMapper domainTemplateMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String EXTERNAL_API_BASE = "https://lctemplates.gitlink.org.cn";

    public TemplateBusiness(TemplateMapper templateMapper,
                            DomainTemplateMapper domainTemplateMapper,
                            RestTemplateBuilder restTemplateBuilder) {
        this.templateMapper = templateMapper;
        this.domainTemplateMapper = domainTemplateMapper;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public List<Template> getAllTemplates() {
        return templateMapper.selectList(null);
    }

    @Transactional
    public void bindDomainAndTemplate(String domainCode, Long templateId) {
        if (domainCode == null || domainCode.isBlank() || templateId == null) {
            throw new RuntimeException("领域编码和模板ID不能为空");
        }
        domainTemplateMapper.insertDomainTemplateRelation(domainCode, templateId);
    }

    @Transactional
    public void unbindDomainAndTemplate(String domainCode, Long templateId) {
        domainTemplateMapper.deleteDomainTemplateRelation(domainCode, templateId);
    }

    public List<Template> getTemplateList(String domainCode) {
        if (domainCode == null || domainCode.isBlank()) {
            throw new RuntimeException("领域编码不能为空");
        }
        return templateMapper.getDomainTemplate(domainCode);
    }

    /**
     * 从外部模板库导入模板到本地
     */
    @Transactional
    public Template importTemplate(Long externalTemplateId) {
        if (externalTemplateId == null) {
            throw new RuntimeException("外部模板ID不能为空");
        }

        // 调用外部 API 获取模板详情
        String url = EXTERNAL_API_BASE + "/templates/" + externalTemplateId;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);

        String responseBody;
        try {
            ResponseEntity<String> resp = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
            responseBody = resp.getBody();
        } catch (Exception e) {
            throw new RuntimeException("调用外部模板库失败: " + e.getMessage());
        }

        if (responseBody == null || responseBody.isBlank()) {
            throw new RuntimeException("外部模板库返回空数据");
        }

        // 解析外部响应
        Map<String, Object> externalData;
        try {
            externalData = objectMapper.readValue(responseBody, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("解析外部模板数据失败: " + e.getMessage());
        }

        // 映射字段到本地 Template 实体
        Template template = mapExternalToTemplate(externalData);

        // 按 template_id 查本地是否已存在
        Template existing = templateMapper.selectByTemplateId(externalTemplateId);
        if (existing != null) {
            throw new RuntimeException("模板ID " + externalTemplateId + " 已存在，不可重复导入");
        }

        // 新增
        templateMapper.insert(template);

        return template;
    }

    public Template getTemplateByTemplateId(Long templateId) {
        Template template = templateMapper.selectByTemplateId(templateId);
        if (template == null) {
            throw new RuntimeException("模板不存在");
        }
        return template;
    }

    public Template createTemplate(Template template) {
        if (template.getName() == null || template.getName().isBlank()) {
            throw new RuntimeException("模板名称不能为空");
        }
        templateMapper.insert(template);
        return template;
    }

    public Template updateTemplateByTemplateId(Long templateId, Template template) {
        Template existing = templateMapper.selectByTemplateId(templateId);
        if (existing == null) {
            throw new RuntimeException("模板不存在");
        }
        template.setId(existing.getId());
        templateMapper.updateById(template);
        return template;
    }

    public void deleteTemplateByTemplateId(Long templateId) {
        Template existing = templateMapper.selectByTemplateId(templateId);
        if (existing == null) {
            throw new RuntimeException("模板不存在");
        }
        templateMapper.deleteById(existing.getId());
    }

    private Template mapExternalToTemplate(Map<String, Object> data) {
        Template t = new Template();
        t.setTemplate_id(toLong(data.get("id")));
        t.setName(toStr(data.get("name")));
        t.setTemplate_index(toStr(data.get("template_index")));
        t.setTemplate_description(toStr(data.get("template_description")));
        t.setExample_image_url(toStr(data.get("example_image_url")));
        t.setCode_url(toStr(data.get("code_url")));
        t.setRepository_url(toStr(data.get("repository_url")));
        t.setFile_source(toStr(data.get("file_source")));
        t.setSubmitter(toStr(data.get("submitter")));
        t.setLicense(toStr(data.get("license")));
        t.setCode_file(toStr(data.get("code_file")));

        // tags: 外部是 JSON 对象，本地存为 JSON 字符串
        Object tagsObj = data.get("tags");
        if (tagsObj != null) {
            try {
                t.setTags(objectMapper.writeValueAsString(tagsObj));
            } catch (Exception e) {
                t.setTags(null);
            }
        }

        // 时间字段：外部是 ISO 字符串
        t.setCreated_at(toDate(data.get("created_at")));
        t.setUpdated_at(toDate(data.get("updated_at")));

        return t;
    }

    private Long toLong(Object val) {
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).longValue();
        try { return Long.parseLong(val.toString()); } catch (Exception e) { return null; }
    }

    private String toStr(Object val) {
        if (val == null) return null;
        return val.toString();
    }

    private Date toDate(Object val) {
        if (val == null) return null;
        try {
            return objectMapper.readValue(val.toString(), Date.class);
        } catch (Exception e) {
            return null;
        }
    }
}