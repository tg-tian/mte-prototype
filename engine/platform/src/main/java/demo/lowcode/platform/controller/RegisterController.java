package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceBusiness;
import demo.lowcode.platform.business.DeviceRegisterBusiness;
import demo.lowcode.platform.business.DeviceTypeBusiness;
import demo.lowcode.platform.business.ScenarioBusiness;
import demo.lowcode.platform.entity.Device;
import demo.lowcode.platform.entity.DeviceRegister;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/register" )
@CrossOrigin
@Api(value = "场景注册接口",tags={"场景注册管理"})
public class RegisterController {

    @Autowired
    private DeviceBusiness deviceBusiness;
    @Resource
    DeviceRegisterBusiness deviceRegisterBusiness;
    @Resource
    ScenarioBusiness scenarioBusiness;
    @Resource
    DeviceTypeBusiness deviceTypeBusiness;

    /**
     * 上传场景绑定数据
     * @param requestBody
     * @param scenarioCode
     * @param deviceName
     * @return
     */
    @PostMapping(value = "/device")
    public ResponseEntity<?> uploadDeviceRegister(@RequestBody Map<String, Object> requestBody, String scenarioCode,String deviceName) {
        //获取 deviceForm 中的数据
        Map<String, Object> deviceForm = (Map<String, Object>) requestBody.get("deviceForm");

        //创建 DeviceRegister 实体对象并填充数据
        DeviceRegister deviceRegister = new DeviceRegister();
        deviceRegister.setDeviceId(deviceBusiness.loadDeviceId((String) deviceForm.get("deviceCode")));
        deviceRegister.setSceneId(scenarioBusiness.loadSceneId(scenarioCode));
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

    /**
     * 获取场景注册数据
     * @param scenarioCode
     * @return
     */
    @GetMapping(value = "/device")
    @ApiOperation(value = "获取场景注册数据",notes = "用于获取该场景下已经注册的设备信息")
    public ResponseEntity<?> loadDeviceRegister(String scenarioCode){
        List<Map<String,String>> resultList = new ArrayList<>(); //构建map存储所需数据
        //根据领域名称获得领域编号
        long sceneId = scenarioBusiness.loadSceneId(scenarioCode);
        //根据领域编号获取注册设备列表
        List<DeviceRegister> deviceRegisterList= deviceRegisterBusiness.getRegisterInfo(sceneId);
        for(DeviceRegister deviceRegister : deviceRegisterList)
        {
            Map<String,String> result = new HashMap<>(); //构建map存储所需数据
            //根据deviceId获取设备信息
            Device devices = deviceBusiness.getById(deviceRegister.getDeviceId());
            result.put("deviceCode",devices.getDeviceCode());
            result.put("deviceName",devices.getDeviceName());
            result.put("deviceType",deviceTypeBusiness.loadDeviceType(devices.getDeviceTypeId()));
            result.put("deviceService",devices.getManufacturer());
            result.put("protocol",deviceRegister.getProtocol());
            result.put("host",deviceRegister.getIPAddress());
            result.put("port",Integer.toString(deviceRegister.getPort()));
            resultList.add(result);
        }
        return new ResponseEntity<>(resultList,HttpStatus.OK);
    }

}
