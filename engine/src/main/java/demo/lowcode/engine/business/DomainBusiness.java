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
    public void addDomain(String domainId, String domainName, List<String> deviceTypeList, Map<String, List<String>> serviceMap) {
        DomainMeta domainMeta = new DomainMeta();
        domainMeta.setDomainId(domainId);
        domainMeta.setDomainName(domainName);
        domainMeta.setDeviceType(deviceTypeList);
        domainMeta.setServiceType(serviceMap);
        System.out.println("加载领域信息："+domainMeta);
    }

    public void loadDomain(String domainPath) throws IOException {
        // 读取该领域的json文件(领域信息、设备类型列表、应用功能列表)
        File file = new File(domainPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        String domainId = rootNode.path("domainId").asText();
        String domainName = rootNode.path("domainName").asText();

        List<String> deviceTypeList = new ArrayList<>();
        Map<String, List<String>> serviceMap = new HashMap<>();
        JsonNode deviceTypeListNode = rootNode.path("deviceTypeList");
        if (deviceTypeListNode.isArray()){
            for (JsonNode deviceTypeNode: deviceTypeListNode){
                String deviceType = deviceTypeNode.path("deviceType").asText();
                JsonNode serviceTypeListNode = deviceTypeNode.path("serviceType");
                List<String> serviceType = new ArrayList<>();
                if (serviceTypeListNode.isArray()) {
                    for (JsonNode serviceNode : serviceTypeListNode) {
                        serviceType.add(serviceNode.asText());
                    }
                }
                // 添加设备类型列表
                deviceTypeList.add(deviceType);
                // 添加设备对应的服务列表
                serviceMap.put(deviceType, serviceType);
            }
        }

        // 新增领域
        addDomain(domainId,domainName,deviceTypeList,serviceMap);

    }

    public void addDeviceType(String deviceType, String domainId) {
        DomainMeta domainMeta = null;
        List<String> deviceTypeList = domainMeta.getDeviceType();
        deviceTypeList.add(deviceType);
        domainMeta.setDeviceType(deviceTypeList);
    }

    public List<String> getDeviceTypeList(String domainId) {
        return new ArrayList<>(Arrays.asList("CoffeeMaker"));
    }

    public void addServiceType(String serviceName, String deviceType, String domainId) {
        DomainMeta domainMeta = null;
        Map<String, List<String>> serviceTypeMap = domainMeta.getServiceType();
        List<String> serviceType = serviceTypeMap.get(deviceType);
        serviceType.add(serviceName);
        serviceTypeMap.put(deviceType, serviceType);
        domainMeta.setServiceType(serviceTypeMap);
    }

    public List<String> getServiceType(String deviceType, String domainId) {
        return new ArrayList<>(Arrays.asList("AService", "BService"));
    }
}
