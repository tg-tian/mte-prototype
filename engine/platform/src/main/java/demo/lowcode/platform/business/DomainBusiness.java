package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.CommonConfig;
import demo.lowcode.common.Property;
import demo.lowcode.platform.dto.DomainJson;
import demo.lowcode.platform.dto.DomainPubInfo;
import demo.lowcode.platform.dto.Domain_ComponentJson;
import demo.lowcode.platform.dto.NewDomain;
import demo.lowcode.platform.entity.ComponentAbout;
import demo.lowcode.platform.entity.Domain;
import demo.lowcode.platform.mapper.DomainMapper;
import demo.lowcode.platform.model.DomainMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class DomainBusiness {

    private final DomainMapper domainMapper;

    @Autowired
    public  DomainBusiness(DomainMapper domainMapper) { this.domainMapper = domainMapper;}

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
        //组件信息表，定义在文件 definition/SmartBuilding.do 使用componentID作为“键”，使用剩下的内容作为“值”的List
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


    public Domain  createDomain(NewDomain newDomain){
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
        domainMapper.deleteById(id);
    }

    public Domain publishDomain(DomainPubInfo pubInfo) {
        Domain existDomain = domainMapper.selectById(pubInfo.getDomainId());
        if (existDomain == null) {
            throw new RuntimeException("领域不存在");
        }

        //生成领域中间表示DSL

        existDomain.setStatus(pubInfo.getStatus());
        existDomain.setUrl(pubInfo.getUrl());
        existDomain.setUpdateTime(new Date());
        domainMapper.updateById(existDomain);

        return existDomain;
    }
}
