package demo.lowcode.platform.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor

@ApiModel(value = "设备信息类")
public class DeviceInformation {
    @ApiModelProperty("设备代码")
    private String deviceCode; // 对应 CoffeeMaker.json 中 "code": "CoffeeMaker",
    @ApiModelProperty("设备名称")
    private String name;//对应 CoffeeMaker.json 中 "name": "咖啡机器人",
    @ApiModelProperty("设备图标路径")
    private String  img_link;//对应 CoffeeMaker.json 中 "img_link": "./CoffeeMaker.png",
    @ApiModelProperty("属性表")
    private Map<String, Object> propertyMap; //存放设备的属性，例如咖啡机的甜度
    @ApiModelProperty("服务列表")
    private DeviceConnectService service; //存储服务，以及服务对应的操作支持 对应 CoffeeMaker.json 中commands下的services
    // private String deviceType;               //?
    //private Map<String, String> eventMap;    //?
    @ApiModelProperty("事件路径")
    private String eventPath;       //对应于CoffeeMaker.json 中refs/refPath
    @ApiModelProperty("服务路径")
    private Map<String,String> servicesPath; // 对应于CoffeeMaker.json 中 键：refs/refPath/name 值：refs/refPath/path
    @ApiModelProperty("输入参数")
    private Map<String,Object> inputParam; // 对应于CoffeeMaker.json 中inputParam
    @ApiModelProperty("输出参数")
    private Map<String,Object> outputParam; // 对应于CoffeeMaker.json 中outputParam
}
