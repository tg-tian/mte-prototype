package demo.lowcode.platform.model;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class Scenario_ResourceJson {
    /*此处用于定义领域中的资源类型*/
    //资源1：设备资源
    private List<Map<String,String>> devicesList;

    private Map<String,Map<String,String>> devices_service;

    //
}
