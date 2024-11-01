package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceTypeBusiness;
import demo.lowcode.platform.entity.DeviceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/device-type")
@CrossOrigin(origins = "http://localhost:2400")
@Api(value = "设备类型接口",tags={"设备类型管理"})
/**
 * 这个注解表示该类是一个 Spring MVC 的控制器，能够处理 HTTP 请求并返回结果。使用 @RestController 表示该控制器的方法默认会返回数据（通常是 JSON 或纯文本），
 * 而不是视图（像传统的 @Controller）。
 * */
public class DeviceTypeController {

    @Resource
    DeviceTypeBusiness deviceTypeBusiness;

    /**
     * 上传设备类型
     * @param deviceType
     * @return
     */
    @PostMapping("/upload")  //应该改成/info
    @ApiOperation(value = "设备类型数据上传", notes = "上传数据类型到数据库")
    public ResponseEntity<?> deviceInfoUpload(@RequestBody DeviceType deviceType){
        System.out.println(deviceType);
        try {
            deviceTypeBusiness.deviceTypeUpload(deviceType);
            return new ResponseEntity<>("设备类型上传成功",HttpStatus.OK);
        }catch (RuntimeException e)
        {
            System.err.println("设备类型上传失败: " + e.getMessage());
            return new ResponseEntity<>("请勿重复上传设备",HttpStatus.CONFLICT);
        }
    }

    /**
     * 读取设备类型数据
     * @param deviceTypeCode
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "读取设备类型数据", notes = "根据设备类型code读取设备数据")
    public ResponseEntity<?> deviceInfoLoad(String deviceTypeCode){
        try {
            DeviceType deviceType = deviceTypeBusiness.loadDeviceTypeInfo(deviceTypeCode);
            return new ResponseEntity<>(deviceType,HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备类型读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到该设备记录",HttpStatus.CONFLICT);
        }
    }

    /**
     * 修改设备发布状态
     * @param deviceTypeCode
     * @return
     */
    @PutMapping ("/publish")
    @ApiOperation(value = "修改设备发布状态", notes = "根据设备类型code修改对应设备的发布状态")
    public ResponseEntity<?> devicePublish(String deviceTypeCode){
        try{
            deviceTypeBusiness.updatePublish(deviceTypeCode);
            return new ResponseEntity<>("设备模版发布成功",HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备模版发布失败: " + e.getMessage());
            return new ResponseEntity<>("设备模版发布失败",HttpStatus.CONFLICT);
        }
    }
}
