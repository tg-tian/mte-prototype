package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.UploadBusiness;
import demo.lowcode.platform.entity.DeviceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/device-type")
@Api(value = "设备类型接口",tags={"设备类型管理"})
/**
 * 这个注解表示该类是一个 Spring MVC 的控制器，能够处理 HTTP 请求并返回结果。使用 @RestController 表示该控制器的方法默认会返回数据（通常是 JSON 或纯文本），
 * 而不是视图（像传统的 @Controller）。
 * */
public class DeviceTypeController {

    private final UploadBusiness uploadBusiness;
    @Autowired
    public DeviceTypeController(UploadBusiness uploadBusiness){
        this.uploadBusiness = uploadBusiness;
    }

    @PostMapping("/upload")
    @ApiOperation(value = "设备类型数据上传", notes = "上传数据类型到数据库")
    public ResponseEntity<?> deviceInfoUpload(@RequestBody DeviceType deviceType){
        System.out.println(deviceType);
        uploadBusiness.deviceTypeUpload(deviceType);
        return new ResponseEntity<>("上传成功", HttpStatus.OK);
    }

}
