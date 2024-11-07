package demo.lowcode.platform;

import demo.lowcode.common.Command;
import demo.lowcode.common.Param;
import lowcode.device.component.business.AddDeviceTypeBusiness;
import lowcode.device.component.entity.BrandService;
import lowcode.device.component.entity.DeviceEvent;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddDeviceTypeTest {
    @Autowired
    AddDeviceTypeBusiness addDeviceTypeBusiness;

    @Test
    public void initDeviceTypeFail(){
        try {
            addDeviceTypeBusiness.initDeviceType("CoffeeMaker", "咖啡机器人", "");
            Assertions.fail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void initDeviceTypeSuccess(){
        addDeviceTypeBusiness.initDeviceType("Conditioner", "空调", "");
    }

    @Test
    public void addCommandSuccess(){
        Command command = new Command("start", "启动");
        command.setOutputParam("void");
        Param param = new Param("time", "启动时间", "String");
//        param.setOptions(List.of("222"));
//        param.setDefaultValue("2024/10/8");
        List<Param> inputs = new ArrayList<>();
        inputs.add(param);
        command.setInputParam(inputs);
        addDeviceTypeBusiness.addCommand("Conditioner", command);
    }

    @Test
    public void addEventSuccess(){
        DeviceEvent event = new DeviceEvent("onStartError", "启动失败", "onError", "start");
        addDeviceTypeBusiness.addEvent("Conditioner", "start", event);
    }

    @Test
    public void addServiceSuccess(){
        BrandService service = new BrandService("AService", "A品牌", "...", "AService.json");
//        addDeviceTypeBusiness.initService("Conditioner", service);
        addDeviceTypeBusiness.editService("Conditioner", "AService");
    }
}
