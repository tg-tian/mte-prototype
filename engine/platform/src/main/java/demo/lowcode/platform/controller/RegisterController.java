package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceBusiness;
import demo.lowcode.platform.business.DeviceRegisterBusiness;
import demo.lowcode.platform.business.ScenarioBusiness;
import demo.lowcode.platform.entity.DeviceRegister;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/register" )
@CrossOrigin(origins = "http://localhost:2400")
@Api(value = "场景注册接口",tags={"场景注册管理"})
public class RegisterController {
    @Resource
    DeviceRegisterBusiness deviceRegisterBusiness;
    @Resource
    DeviceBusiness deviceBusiness;
    @Resource
    ScenarioBusiness scenarioBusiness;

    @PostMapping(value = "/device")
    public ResponseEntity<?> uploadDeviceRegister(@RequestBody Map<String, Object> requestBody, String scenarioName) {
        //获取 deviceForm 中的数据
        Map<String, Object> deviceForm = (Map<String, Object>) requestBody.get("deviceForm");

        //创建 DeviceRegister 实体对象并填充数据
        DeviceRegister deviceRegister = new DeviceRegister();
        deviceRegister.setDeviceId(deviceBusiness.getDeviceId((String) deviceForm.get("deviceCode"))); //暂时这样写
        deviceRegister.setSceneId(scenarioBusiness.getSceneId(scenarioName)); //暂时这样写
        deviceRegister.setProtocol((String) deviceForm.get("protocol"));
        deviceRegister.setIPAddress((String) deviceForm.get("host"));
        deviceRegister.setPort(Integer.parseInt(deviceForm.get("port").toString()));
        //注册设备
       try {
           deviceRegisterBusiness.uploadRegisterData(deviceRegister);
           return new ResponseEntity<>("设备注册成功", HttpStatus.OK);
        }catch (RuntimeException e)
        {
            System.err.println("设备注册失败: " + e.getMessage());
            return new ResponseEntity<>("请勿重复注册设备",HttpStatus.CONFLICT);
        }
    }

}
