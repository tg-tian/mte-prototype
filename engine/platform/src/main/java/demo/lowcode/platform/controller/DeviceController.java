package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceBusiness;
import demo.lowcode.platform.business.DeviceTypeBusiness;
import demo.lowcode.platform.entity.Device;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/device")
@CrossOrigin
@Api(value = "设备接口",tags={"设备实例管理"})
public class DeviceController {
    @Resource
    DeviceBusiness deviceBusiness;

    @Resource
    DeviceTypeBusiness deviceTypeBusiness;

    @PostMapping(value = "/data")
    public ResponseEntity<?> deviceInfoUpload(@RequestBody Map<String, Object> requestBody) {
        try {
            // 获取 deviceForm 中的数据
            Map<String, Object> deviceForm = (Map<String, Object>) requestBody.get("deviceForm");
            Device device = new Device();
            device.setDeviceCode((String) deviceForm.get("deviceCode"));
            device.setDeviceName((String) deviceForm.get("deviceName"));;
            // 获取 deviceType 对象并填充设备类型信息
            Map<String, Object> deviceType = (Map<String, Object>) deviceForm.get("deviceType");
            device.setDeviceTypeId(deviceTypeBusiness.loadDeiceTypeId((String) deviceType.get("code")));
            // 获取 deviceService 的品牌服务数据
            Map<String, Object> deviceService = (Map<String, Object>) deviceForm.get("deviceService");
            device.setManufacturer((String) deviceService.get("code"));
            device.setVersionNumber("1.0.0");
            deviceBusiness.deviceUpload(device);

            return new ResponseEntity<>("设备上传成功", HttpStatus.OK);
        } catch (RuntimeException e) {
            System.err.println("设备上传失败: " + e.getMessage());
            return new ResponseEntity<>("请勿重复上传设备", HttpStatus.CONFLICT);
        }
    }

}
