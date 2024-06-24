package demo.lowcode.engine.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.engine.model.DomainMeta;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class DomainBusiness {
    // domain增删改查
    public DomainMeta addDomain(String domainId, String domainName, Map<String, Map<String, List<String>>> componentMap) {
        DomainMeta domainMeta = new DomainMeta();
        domainMeta.setDomainId(domainId);
        domainMeta.setDomainName(domainName);
        domainMeta.setComponentType(componentMap);
        System.out.println("加载领域信息："+domainMeta);
        return domainMeta;
    }

    public DomainMeta loadDomain(String domainPath) throws IOException {
        // 读取该领域的json文件(领域信息、设备类型列表、应用功能列表)
        File file = new File(domainPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        String domainId = rootNode.path("domainId").asText();
        String domainName = rootNode.path("domainName").asText();

        Map<String, Map<String, List<String>>> componentMap = new HashMap<>();
        JsonNode componentTypeListNode = rootNode.path("componentTypeList");
        if (componentTypeListNode.isArray()){
            for (JsonNode componentTypeNode: componentTypeListNode){
                String componentName = componentTypeNode.path("componentName").asText();
                String type = componentTypeNode.path("type").asText();
                JsonNode serviceTypeListNode = componentTypeNode.path("serviceType");
                List<String> serviceType = new ArrayList<>();
                if (serviceTypeListNode.isArray()) {
                    for (JsonNode serviceNode : serviceTypeListNode) {
                        serviceType.add(serviceNode.asText());
                    }
                }

                if (componentMap.containsKey(type)){
                    Map<String, List<String>> componentTypes = componentMap.get(type);
                    if (componentTypes.containsKey(componentName)){
                        System.out.println("领域内存在重复组件");
                    }else {
                        componentTypes.put(componentName, serviceType);
                    }
                }else {
                    componentMap.put(type, new HashMap<>(){{
                        put(componentName, serviceType);
                    }});
                }
            }
        }

        // 新增领域
        return addDomain(domainId,domainName,componentMap);
    }

    public void addComponentType(String componentName, String type, String domainId, List<String> services) {
        DomainMeta domainMeta = null;
        Map<String, Map<String, List<String>>> componentTypeList = domainMeta.getComponentType();
        if (componentTypeList.containsKey(type)) {
            Map<String, List<String>> componentTypes = componentTypeList.get(type);
            if (componentTypes.containsKey(componentName)){
                System.out.println("领域内存在重复组件");
            }else {
                componentTypes.put(componentName, services);
            }
        }else {
            componentTypeList.put(type, new HashMap<>(){{
                put(componentName, services);
            }});
        }
        domainMeta.setComponentType(componentTypeList);
    }

    public Map<String, List<String>> getDeviceTypeList(String domainId) {
//        DomainMeta domainMeta = null;
//        Map<String, Map<String, List<String>>> componentTypeList = domainMeta.getComponentType();
//        return componentTypeList.get("Device");
        return new HashMap<>(){{
            put("CoffeeMaker", new ArrayList<>(Arrays.asList("AService", "BService")));
        }};
    }

    public List<String> getDeviceServiceType(String deviceType, String domainId) {
        return new ArrayList<>(Arrays.asList("AService", "BService"));
    }
}
