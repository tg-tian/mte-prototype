package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import demo.lowcode.platform.common.CommonConfig;
import demo.lowcode.platform.common.Property;
import demo.lowcode.platform.dto.*;
import demo.lowcode.platform.entity.*;
import demo.lowcode.platform.mapper.DomainMapper;
import demo.lowcode.platform.mapper.TemplateMapper;
import demo.lowcode.platform.mapper.DomainComponentMapper;
import demo.lowcode.platform.model.DomainMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class DomainBusiness {

    private final DomainMapper domainMapper;
    private final TemplateMapper templateMapper;
    private final DomainComponentMapper domainComponentMapper;

    @Autowired
    public  DomainBusiness(DomainMapper domainMapper, TemplateMapper templateMapper, DomainComponentMapper domainComponentMapper) {
        this.domainMapper = domainMapper;
        this.templateMapper = templateMapper;
        this.domainComponentMapper = domainComponentMapper;
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
        Domain existDomain = domainMapper.getDomainByCode(newDomain.getCode());
        if (existDomain != null) {
            throw new RuntimeException("领域编码已存在");
        }

        Domain domain = new Domain();
        domain.setDomainCode(newDomain.getCode());
        domain.setDomainName(newDomain.getName());
        domain.setDomainDescription(newDomain.getDescription());
        domain.setStatus(newDomain.getStatus());
        domain.setCreateTime(new Date());
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
        domain.setDomainName(newDomain.getName());
        domain.setDomainDescription(newDomain.getDescription());
        domain.setStatus(newDomain.getStatus());
        domain.setUpdateTime(new Date());
        domain.setUrl(newDomain.getUrl());
        domain.setCodeEditor(newDomain.getCodeEditor());
        domain.setModelEditor(newDomain.getModelEditor());
        domain.setFramework(newDomain.getBaseFramework());
        domain.setDsl(newDomain.getDslStandard());
        domainMapper.updateById(domain); // 第一个参数传 null
        return domain;
    }

    public void deleteDomainByID(Long id){
        Domain existDomain = domainMapper.selectById(id);
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }
        domainMapper.deleteById(id);
    }

    public Domain publishDomain(DomainPubInfo pubInfo) {
        Domain existDomain = domainMapper.selectById(pubInfo.getDomainId());
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }

        if (pubInfo.getDslData() != null){
            // 存储领域配置文件
            writeDomainInfo(pubInfo.getDslData());
        }

        // 取消发布，删除领域配置文件
        if (Objects.equals(pubInfo.getStatus(), "0")){
            deleteDomainInfo(existDomain.getDomainCode());
        }

        existDomain.setStatus(pubInfo.getStatus());
        existDomain.setUrl(pubInfo.getUrl());
        existDomain.setUpdateTime(new Date());
        domainMapper.updateById(existDomain);

        return existDomain;
    }

    public void writeDomainInfo(DomainTemInfo temInfo){
        try {
            ObjectMapper mapper = new ObjectMapper();
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
            } else {
                throw new RuntimeException("文件不存在，无法删除: " + file.getAbsolutePath());
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
            return new FileSystemResource(filePath);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("领域配置文件不存在: " + e.getMessage());
        }
    }

    public List<DomainTemInfo> getDomainTemplates(){
        List<DomainTemInfo> domainTemInfoList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

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

    public void updateDomainTemplate(DomainTemplateInfo domainTemInfo) {
        Domain existDomain = domainMapper.selectById(domainTemInfo.getDomainId());
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }
        existDomain.setDomainTemplateId(domainTemInfo.getTemplateId());
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
        domain.setDomainName(domainTemInfo.getDomainData().getName());
        domain.setDomainDescription(domainTemInfo.getDomainData().getDescription());
        domain.setStatus(domainTemInfo.getDomainData().getStatus());
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
            templateMapper.batchInsertDomainTemplateRelations(domainId, templateIds);
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

        return domain;
    }
}
