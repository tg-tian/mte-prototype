package demo.lowcode.engine.model;

import cn.hutool.core.annotation.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor

//设备信息
public class DeviceInformation {
    private String deviceCode; // 对应 CoffeeMaker.json 中 "code": "CoffeeMaker",
    private String name;//对应 CoffeeMaker.json 中 "name": "咖啡机器人",
    private String  img_link;//对应 CoffeeMaker.json 中 "img_link": "./CoffeeMaker.png",
    private Map<String, Object> propertyMap; //存放设备的属性，例如咖啡机的甜度
    private DeviceConnectService service; //存储服务，以及服务对应的操作支持 对应 CoffeeMaker.json 中commands下的services
    private String deviceType;
    private Map<String, String> eventMap;
    private String eventPath;
}
