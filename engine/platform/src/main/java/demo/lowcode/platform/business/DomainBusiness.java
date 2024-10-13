package demo.lowcode.platform.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.common.Property;
import demo.lowcode.platform.dto.DomainJson;
import demo.lowcode.platform.dto.Domain_ComponentJson;
import demo.lowcode.platform.entity.ComponentAbout;
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

    /**
     * 基于领域名称获取领域号
     * @param domainName
     * @return
     */
    public long getDomainId (String domainName){
        long domainId = domainMapper.getDomainId(domainName);
        return  domainId;
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
    public DomainMeta loadDomain(String domainPath) throws IOException {
        // 读取该领域信息
        File file = new File(domainPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        String domainId = rootNode.path("domainId").asText();
        String domainName = rootNode.path("domainName").asText();

        Map<String, List<String>> componentMap = new HashMap<>();
        JsonNode componentTypeListNode = rootNode.path("componentTypeList");
        if (componentTypeListNode.isArray()){
            for (JsonNode componentTypeNode: componentTypeListNode){
                String componentName = componentTypeNode.path("componentName").asText();
                String type = componentTypeNode.path("type").asText();

                if (componentMap.containsKey(type)){
                    List<String> componentTypes = componentMap.get(type);
                    if (componentTypes.contains(componentName)){
                        System.out.println("领域内存在重复组件");
                    }else {
                        componentTypes.add(componentName); //?
                    }
                }else {
                    componentMap.put(type, new ArrayList<>(List.of(componentName)));
                }
            }
        }

        // 新增领域
        return addDomain(domainId,domainName,componentMap);
    }

    public Domain_ComponentJson loadComponentJson(String componentType) throws IOException {
        File file = new File("definition/SmartBuilding.do");//获取文件夹
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

    public DomainJson loadJson() throws IOException {
        // 读取该领域的json文件(领域ID、名称、字段表、组件类型列表)
            //获取json文件
        File file = new File("definition/SmartBuilding.do");  //获取文件类
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

    public void addComponentType(String componentName, String type, String domainId) {
        DomainMeta domainMeta = null;
        Map<String, List<String>> componentTypeList = domainMeta.getComponentType();
        if (componentTypeList.containsKey(type)) {
            List<String> componentTypes = componentTypeList.get(type);
            if (componentTypes.contains(componentName)){
                System.out.println("领域内存在重复组件");
            }else {
                componentTypes.add(componentName);
            }
        }else {
            componentTypeList.put(type, new ArrayList<>(List.of(componentName)));
        }
        domainMeta.setComponentType(componentTypeList);
    }

    public List<String> getDeviceTypeList(String domainId) {
        return new ArrayList<>(List.of("CoffeeMaker"));
    }
}
