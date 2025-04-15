package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceBusiness;
import demo.lowcode.platform.entity.Device;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.License;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/devices")
@CrossOrigin
@Api(value = "设备接口",tags={"设备实例管理"})
public class DeviceController {

    @Resource
    DeviceBusiness deviceBusiness;
    @GetMapping("/")
    @ApiOperation(value = "获取设备列表",notes = "获取设备列表")
    public ResponseEntity<?> getDeiceList(@RequestParam Long sceneId){
        List<Device> deviceList =
    }
}
