package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import demo.lowcode.platform.common.CommonConfig;
import demo.lowcode.platform.common.Property;
import demo.lowcode.platform.dto.*;
import demo.lowcode.platform.entity.*;
import demo.lowcode.platform.mapper.DomainTemplateMapper;
import demo.lowcode.platform.mapper.DomainMapper;
import demo.lowcode.platform.mapper.TemplateMapper;
import demo.lowcode.platform.mapper.DomainComponentMapper;
import demo.lowcode.platform.mapper.ComponentMapper;
import demo.lowcode.platform.mapper.DeviceModelMapper;
import demo.lowcode.platform.mapper.DomainDeviceModelMapper;
import demo.lowcode.platform.model.DomainMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DomainBusiness {

    private final DomainMapper domainMapper;
    private final TemplateMapper templateMapper;
    private final DomainTemplateMapper domainTemplateMapper;
    private final DomainComponentMapper domainComponentMapper;
    private final ComponentMapper componentMapper;
    private final DeviceModelMapper deviceModelMapper;
    private final DomainDeviceModelMapper domainDeviceModelMapper;

    @Autowired
    public  DomainBusiness(
            DomainMapper domainMapper,
            TemplateMapper templateMapper,
            DomainTemplateMapper domainTemplateMapper,
            DomainComponentMapper domainComponentMapper,
            ComponentMapper componentMapper,
            DeviceModelMapper deviceModelMapper,
            DomainDeviceModelMapper domainDeviceModelMapper
    ) {
        this.domainMapper = domainMapper;
        this.templateMapper = templateMapper;
        this.domainTemplateMapper = domainTemplateMapper;
        this.domainComponentMapper = domainComponentMapper;
        this.componentMapper = componentMapper;
        this.deviceModelMapper = deviceModelMapper;
        this.domainDeviceModelMapper = domainDeviceModelMapper;
    }

    // domain增删改查
    public DomainMeta addDomain(String domainId, String domainName, Map<String, List<String>> componentMap) {
        DomainMeta domainMeta = new DomainMeta();
        domainMeta.setDomainId(domainId);
        domainMeta.setDomainName(domainName);
        domainMeta.setComponentType(componentMap);
        System.out.println("加载领域信息："+domainMeta);
        return domainMeta;
    }

    public  DomainJson addDomainJson(String domainId, String domainName, Map<String, Property> domainField){
        DomainJson domainJson = new DomainJson();
        domainJson.setDomainID(domainId);
        domainJson.setDomainName(domainName);
        domainJson.setDomainField(domainField.values().stream().toList());
        System.out.println("正在加载领域信息："+ domainJson);
        return domainJson;
    }

    public Domain_ComponentJson addComponentJson(String componentType, Map<String, ComponentAbout> componentAbout){
        Domain_ComponentJson domain_componentJson = new Domain_ComponentJson();
        domain_componentJson.setComponentType(componentType);
        domain_componentJson.setComponentAbout(componentAbout.values().stream().toList());
        System.out.println("正在加载领域"+componentType+"信息：");
        return domain_componentJson;
    }

    public Domain_ComponentJson loadComponentJson(String componentType, String domainCode) throws IOException {
        File file = new File(CommonConfig.getWorkspacePath()+domainCode+"/"+domainCode+".do");//获取文件夹
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        //组件信息表，定义在文件 definition/SmartBuilding.do 使用componentID作为"键"，使用剩下的内容作为"值"的List
        Map<String,ComponentAbout> componentMap = new HashMap<>(); //用于存储componentAbout

        JsonNode componentTypeListNode = rootNode.path("componentTypeList"); //获取组件列表
        if(componentTypeListNode.isArray()){
            for(JsonNode componentTypeNode : componentTypeListNode){
                if(Objects.equals(componentTypeNode.path("componentType").asText(), componentType)){ //找寻对应的组件
                    JsonNode componentAboutNode = componentTypeNode.path("componentAbout");
                    for(JsonNode componentAbout : componentAboutNode){ //遍历componentAbout
                        /*
                          在下方添加json文件componentAbout的组件。
                          */
                        ComponentAbout componentAboutInfo = new ComponentAbout();

                        JsonNode componentIDNode = componentAbout.path("componentID"); //组件ID
                        String componentID = componentIDNode.asText();
                        componentAboutInfo.setComponentId(componentID);

                        JsonNode componentNameNode = componentAbout.path("componentName"); //组件名称
                        if(!componentNameNode.isMissingNode()) componentAboutInfo.setComponentName(componentNameNode.asText());

                        JsonNode imgPathNode = componentAbout.path("imgPath"); //图像路径
                        if(!imgPathNode.isMissingNode()) componentAboutInfo.setImgPath(imgPathNode.asText());

                        JsonNode componentBriefNode = componentAbout.path("componentBrief"); //流程简介
                        if(!componentBriefNode.isMissingNode()) componentAboutInfo.setBrief(componentBriefNode.asText());

                        componentMap.put(componentID,componentAboutInfo); //导入集合
                    }
                }
            }
        }
        return addComponentJson(componentType,componentMap);
    }

    public DomainJson loadJson(String domainCode) throws IOException {
        // 读取该领域的json文件(领域ID、名称、字段表、组件类型列表)
            //获取json文件
        File file = new File(CommonConfig.getWorkspacePath()+domainCode+"/"+domainCode+".do");  //获取文件类
        ObjectMapper objectMapper = new ObjectMapper(); //?
        JsonNode rootNode = objectMapper.readTree(file); //获取json树状结构
            //组织json文件
        String domainId = rootNode.path("domainId").asText(); //ID
        String domainName = rootNode.path("domainName").asText(); //名称

                //获取字段表内容
        Map<String, Property> domainFieldMap = new HashMap<>(); //字段表集合
        JsonNode domainFieldListNode = rootNode.path("domainField"); //嵌套获取字段表内容
        if(domainFieldListNode.isArray()){//如果字段表列表非空
            for(JsonNode domainFieldNode : domainFieldListNode){ //遍历字段列表
                String fieldID = domainFieldNode.path("fieldID").asText(); //字段ID
                String fieldName = domainFieldNode.path("fieldName").asText(); //字段名称
                String  type= domainFieldNode.path("type").asText(); //字段数据类型
                //判断重复字段
                if(domainFieldMap.containsKey(fieldID)){
                    System.out.println("领域内存在重复组件");
                }
                else{
                    Property property = new Property(fieldID, fieldName, type);
                    domainFieldMap.put(fieldID,property); //domain字段集合写入
                }
            }
        }
        return  addDomainJson(domainId,domainName,domainFieldMap);
    }

    public List<Domain> getDomainList() {
        return domainMapper.selectList(null);
    }

    public  Domain getDomainByID(Long id){
        return domainMapper.selectById(id);
    }


    public Domain createDomain(NewDomain newDomain){
        if (newDomain.getCode() == null || newDomain.getCode().trim().isEmpty()) {
            throw new RuntimeException("领域编码不能为空");
        }
        if (newDomain.getName() == null || newDomain.getName().trim().isEmpty()) {
            throw new RuntimeException("领域名称不能为空");
        }
        Domain existDomain = domainMapper.getDomainByCode(newDomain.getCode());
        if (existDomain != null) {
            throw new RuntimeException("领域编码已存在");
        }

        Domain domain = new Domain();
        domain.setDomainCode(newDomain.getCode().trim());
        domain.setDomainName(normalizeText(newDomain.getName()).trim());
        domain.setDomainDescription(normalizeText(newDomain.getDescription()));
        domain.setStatus(DomainStatus.DEVELOPING.getCode());
        Date now = new Date();
        domain.setCreateTime(now);
        domain.setUpdateTime(now);
        domain.setCodeEditor(newDomain.getCodeEditor());
        domain.setModelEditor(newDomain.getModelEditor());
        domain.setFramework(newDomain.getBaseFramework());
        domain.setDsl(newDomain.getDslStandard());
        domainMapper.insert(domain);
        return domain;
    }

    public Domain changeDomainByID(long id, NewDomain newDomain){
        Domain existDomain = domainMapper.selectById(id);
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }

        Domain domain = new Domain();
        domain.setDomainId(id);
        domain.setDomainCode(newDomain.getCode());
        domain.setDomainName(normalizeText(newDomain.getName()));
        domain.setDomainDescription(normalizeText(newDomain.getDescription()));
        domain.setStatus(DomainStatus.normalizeCode(newDomain.getStatus()));
        domain.setUpdateTime(new Date());
        domain.setUrl(newDomain.getUrl());
        domain.setCodeEditor(newDomain.getCodeEditor());
        domain.setModelEditor(newDomain.getModelEditor());
        domain.setFramework(newDomain.getBaseFramework());
        domain.setDsl(newDomain.getDslStandard());
        domainMapper.updateById(domain); // 第一个参数传 null
        return domain;
    }

    @Transactional
    public void deleteDomainByID(Long id){
        Domain existDomain = domainMapper.selectById(id);
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }
        domainTemplateMapper.deleteDomainTemplateRelationsByDomainId(id);
        domainComponentMapper.deleteDomainComponentsByDomainId(id);
        domainDeviceModelMapper.deleteDomainDeviceModelRelationsByDomainId(id);
        domainMapper.deleteById(id);
    }

    public Domain publishDomain(Long domainId) {
        if (domainId == null) {
            throw new RuntimeException("领域ID不能为空");
        }
        Domain existDomain = domainMapper.selectById(domainId);
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }

        String targetStatus = DomainStatus.PUBLISHED.getCode();
        String targetUrl = existDomain.getUrl();

        // 发布时由后端统一组装导出数据，确保携带模板/设备模型/组件绑定信息。
        if (!DomainStatus.isDeveloping(targetStatus)) {
            writeDomainInfo(buildDomainExportInfo(existDomain, null, targetStatus, targetUrl));
        }

        // 取消发布，删除领域配置文件
        if (DomainStatus.isDeveloping(targetStatus)){
            deleteDomainInfo(existDomain.getDomainCode());
        }

        existDomain.setStatus(DomainStatus.normalizeCode(targetStatus));
        existDomain.setUrl(targetUrl);
        existDomain.setUpdateTime(new Date());
        domainMapper.updateById(existDomain);

        return existDomain;
    }


    public void writeDomainInfo(DomainTemInfo temInfo){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.enable(SerializationFeature.INDENT_OUTPUT); // 格式化 JSON

            // 计算目标路径
            String projectRoot = System.getProperty("user.dir"); // 获取项目根目录
            String targetDir = Paths.get(projectRoot,  "template", "domain").toString();
            // 确保目录存在
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs(); // 创建所有不存在的父目录
            }

            File file = new File(targetDir, temInfo.getDomainData().getCode()+".json");

            //写文件
            mapper.writeValue(file, temInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("保存领域配置失败 " + e.getMessage());
        }
    }

    public void deleteDomainInfo(String domainCode){
        try {
            // 计算目标路径
            String projectRoot = System.getProperty("user.dir"); // 获取项目根目录
            String targetDir = Paths.get(projectRoot,  "template", "domain").toString();

            File file = new File(targetDir, domainCode+".json");
            // 删除文件
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("文件删除成功: " + file.getAbsolutePath());
                } else {
                    throw new RuntimeException("文件删除失败: " + file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除领域配置失败 " + e.getMessage());
        }
    }

    public Resource downloadDomain(Long id) {
        Domain existDomain = domainMapper.selectById(id);
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }

        try {
            String projectRoot = System.getProperty("user.dir");
            String targetDir = Paths.get(projectRoot, "template", "domain").toString();
            Path filePath = Paths.get(targetDir, existDomain.getDomainCode() + ".json");
            FileSystemResource resource = new FileSystemResource(filePath);
            if (!resource.exists()) {
                throw new RuntimeException("领域配置文件不存在");
            }
            return resource;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("领域配置文件不存在: " + e.getMessage());
        }
    }

    public List<DomainTemInfo> getDomainTemplates(){
        List<DomainTemInfo> domainTemInfoList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        try {
            // 获取模板目录路径
            String projectRoot = System.getProperty("user.dir");
            String targetDir = Paths.get(projectRoot, "template", "domain").toString();
            File directory = new File(targetDir);
            if (!directory.exists() || !directory.isDirectory()) {
                return domainTemInfoList;
            }

            //构建json列表
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
            if (files == null || files.length == 0) {
                return domainTemInfoList;
            }

            //解析JSON文件
            for (File file : files) {
                DomainTemInfo template = mapper.readValue(file, DomainTemInfo.class);
                domainTemInfoList.add(template);
            }
        } catch (Exception e) {
            System.err.println("获取模板列表失败: " + e.getMessage());
            e.printStackTrace();
        }
        return  domainTemInfoList;
    }

    @Transactional
    public void updateDomainTemplate(DomainTemplateInfo domainTemInfo) {
        Domain existDomain = domainMapper.selectById(domainTemInfo.getDomainId());
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }
        domainTemplateMapper.deleteDomainTemplateRelationsByDomainId(domainTemInfo.getDomainId());
        domainTemplateMapper.insertDomainTemplateRelation(domainTemInfo.getDomainId(), domainTemInfo.getTemplateId());
        existDomain.setUpdateTime(new Date());
        domainMapper.updateById(existDomain);
    }

    @Transactional
    public Domain createDomainFromTemplate(DomainTemInfo domainTemInfo) {
        Domain domain = new Domain();
        if (domainMapper.getDomainByCode(domainTemInfo.getDomainData().getCode()) != null){
            throw new RuntimeException("领域编码已存在");
        }

        // step1: 存入基本信息到数据库，获取id
        domain.setDomainCode(domainTemInfo.getDomainData().getCode());
        domain.setDomainName(normalizeText(domainTemInfo.getDomainData().getName()));
        domain.setDomainDescription(normalizeText(domainTemInfo.getDomainData().getDescription()));
        domain.setStatus(DomainStatus.normalizeCode(domainTemInfo.getDomainData().getStatus()));
        domain.setCreateTime(new Date());
        domain.setCodeEditor(domainTemInfo.getDomainData().getCodeEditor());
        domain.setModelEditor(domainTemInfo.getDomainData().getModelEditor());
        domain.setFramework(domainTemInfo.getDomainData().getBaseFramework());
        domain.setDsl(domainTemInfo.getDomainData().getDslStandard());
        domainMapper.insert(domain);
        Long domainId = domain.getDomainId();

        // step2: 存入和模板的绑定关系到数据库
        List<Long> templateIds = new ArrayList<>();
        for(NewTemplate template : domainTemInfo.getTemplates()){
            if(template.getTemplate_id() == null){
                Template existTemplate = templateMapper.selectByTemplateId(template.getId());
                if (existTemplate!=null){
                    templateIds.add(existTemplate.getId());
                }else{
                    // 该模板未保存过，保存到数据库
                    Template newTemplate = new Template();
                    newTemplate.setTemplate_id(template.getId());
                    newTemplate.setCategory(template.getCategory());
                    newTemplate.setDescribing_the_model(template.getDescribing_the_model());
                    newTemplate.setDescription(template.getDescription());
                    newTemplate.setDomain(template.getDomain());
                    newTemplate.setImage_url(template.getImage_url());
                    newTemplate.setName(template.getName());
                    newTemplate.setTags(template.getTags());
                    newTemplate.setUrl(template.getUrl());
                    templateMapper.insert(newTemplate);
                    templateIds.add(newTemplate.getId());
                }
            } else {
                templateIds.add(template.getId());
            }
        }

        // 批量插入领域-模板关系
        if (!templateIds.isEmpty()) {
            domainTemplateMapper.batchInsertDomainTemplateRelations(domainId, templateIds);
        }

        // step3：存入和组件的绑定关系到数据库
        List<Long> componentIds = new ArrayList<>();
        for(ComponentDto component : domainTemInfo.getComponents()){
            // 收集所有组件ID
            componentIds.add(component.getId());
        }

        // 批量插入领域-组件关系
        if (!componentIds.isEmpty()) {
            domainComponentMapper.batchInsertDomainComponents(domainId, componentIds);
        }

        // step4: 存入和设备模型的绑定关系到数据库
        if (domainTemInfo.getDeviceTypes() != null && !domainTemInfo.getDeviceTypes().isEmpty()) {
            for (DeviceModel deviceType : domainTemInfo.getDeviceTypes()) {
                if (deviceType.getId() == null) {
                    continue;
                }
                if (domainDeviceModelMapper.selectByDomainAndDeviceModel(domainId, deviceType.getId()) != null) {
                    continue;
                }
                domainDeviceModelMapper.insertDomainDeviceModelRelation(domainId, deviceType.getId());
            }
        }

        return domain;
    }

    private DomainTemInfo buildDomainExportInfo(Domain domain, DomainTemInfo requestDslData, String targetStatus, String targetUrl) {
        DomainTemInfo exportInfo = new DomainTemInfo();
        exportInfo.setDomainData(buildDomainData(domain, requestDslData, targetStatus, targetUrl));
        exportInfo.setTemplates(buildTemplateExportList(domain.getDomainId()));
        exportInfo.setDeviceTypes(deviceModelMapper.selectByDomainId(domain.getDomainId()));
        exportInfo.setComponents(buildComponentExportList(domain.getDomainId()));
        return exportInfo;
    }

    private NewDomain buildDomainData(Domain domain, DomainTemInfo requestDslData, String targetStatus, String targetUrl) {
        NewDomain domainData = requestDslData != null && requestDslData.getDomainData() != null
                ? requestDslData.getDomainData()
                : new NewDomain();
        domainData.setCode(domain.getDomainCode());
        domainData.setName(domain.getDomainName());
        domainData.setDescription(domain.getDomainDescription());
        domainData.setCodeEditor(domain.getCodeEditor());
        domainData.setModelEditor(domain.getModelEditor());
        domainData.setBaseFramework(domain.getFramework());
        domainData.setDslStandard(domain.getDsl());
        domainData.setStatus(DomainStatus.normalizeCode(targetStatus));
        domainData.setUrl(targetUrl);
        return domainData;
    }

    private List<NewTemplate> buildTemplateExportList(Long domainId) {
        List<Template> templates = templateMapper.getDomainTemplate(domainId);
        List<NewTemplate> exportTemplates = new ArrayList<>();
        for (Template template : templates) {
            NewTemplate exportTemplate = new NewTemplate();
            // 保持与历史导入语义一致: id 表示模板库ID。
            exportTemplate.setId(template.getTemplate_id());
            exportTemplate.setName(template.getName());
            exportTemplate.setDescription(template.getDescription());
            exportTemplate.setCategory(template.getCategory());
            exportTemplate.setTags(template.getTags());
            exportTemplate.setDomain(template.getDomain());
            exportTemplate.setImage_url(template.getImage_url());
            exportTemplate.setDescribing_the_model(template.getDescribing_the_model());
            exportTemplate.setUrl(template.getUrl());
            exportTemplates.add(exportTemplate);
        }
        return exportTemplates;
    }

    private List<ComponentDto> buildComponentExportList(Long domainId) {
        List<Component> components = componentMapper.selectByDomainId(domainId);
        List<ComponentDto> exportComponents = new ArrayList<>();
        for (Component component : components) {
            exportComponents.add(convertComponentToDto(component));
        }
        return exportComponents;
    }

    private ComponentDto convertComponentToDto(Component component) {
        ComponentDto dto = new ComponentDto();
        dto.setId(component.getComponentId());
        dto.setCode(component.getComponentCode());
        dto.setName(component.getComponentName());
        dto.setDescription(component.getComponentDescription());
        dto.setType(component.getComponentType());
        dto.setPurpose(component.getComponentPurpose());

        ObjectMapper mapper = new ObjectMapper();
        try {
            if (component.getConstraints() != null && !component.getConstraints().isEmpty()) {
                Map<String, Object> constraints = mapper.readValue(component.getConstraints(), Map.class);
                if ("node".equals(component.getComponentType())) {
                    if (constraints.containsKey("inputConstraint")) {
                        dto.setInputConstraint(mapper.convertValue(constraints.get("inputConstraint"), ConstraintDto.class));
                    }
                    if (constraints.containsKey("outputConstraint")) {
                        dto.setOutputConstraint(mapper.convertValue(constraints.get("outputConstraint"), ConstraintDto.class));
                    }
                } else {
                    if (constraints.containsKey("startConstraint")) {
                        dto.setStartConstraint(mapper.convertValue(constraints.get("startConstraint"), ConstraintDto.class));
                    }
                    if (constraints.containsKey("endConstraint")) {
                        dto.setEndConstraint(mapper.convertValue(constraints.get("endConstraint"), ConstraintDto.class));
                    }
                }
            }
            if (component.getProperties() != null && !component.getProperties().isEmpty()) {
                dto.setProperties(mapper.readValue(component.getProperties(), Map.class));
            }
            if (component.getInputs() != null && !component.getInputs().isEmpty()) {
                dto.setInputs(mapper.readValue(component.getInputs(), List.class));
            }
            if (component.getOutputs() != null && !component.getOutputs().isEmpty()) {
                dto.setOutputs(mapper.readValue(component.getOutputs(), List.class));
            }
        } catch (IOException e) {
            throw new RuntimeException("组件导出失败", e);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (component.getCreateTime() != null) {
            dto.setCreateTime(dateFormat.format(component.getCreateTime()));
        }
        if (component.getUpdateTime() != null) {
            dto.setUpdateTime(dateFormat.format(component.getUpdateTime()));
        }
        return dto;
    }

    private String normalizeText(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        String repaired = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        if (containsMojibake(value) && containsChinese(repaired)) {
            return repaired;
        }
        return value;
    }

    private boolean containsMojibake(String value) {
        return value.contains("Ã") || value.contains("æ") || value.contains("å") || value.contains("ç");
    }

    private boolean containsChinese(String value) {
        for (int i = 0; i < value.length(); i++) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(value.charAt(i));
            if (block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                    || block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) {
                return true;
            }
        }
        return false;
    }
}
