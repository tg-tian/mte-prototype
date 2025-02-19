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
@CrossOrigin
@Api(value = "设备类型接口",tags={"设备类型管理"})
public class DeviceTypeController {

    /**
     * TODO:同步iot平台设备类型
     * @param deviceType
     * @return
     */
    @PostMapping("/upload")  //应该改成/info
    @ApiOperation(value = "设备类型数据同步", notes = "上传数据类型到数据库")
    public ResponseEntity<?> deviceInfoUpload(@RequestBody DeviceType deviceType){
        try {
            return new ResponseEntity<>("设备类型同步成功",HttpStatus.OK);
        }catch (RuntimeException e)
        {
            System.err.println("设备类型上传失败: " + e.getMessage());
            return new ResponseEntity<>("请勿重复上传设备",HttpStatus.CONFLICT);
        }
    }

    /**
     * TODO:读取设备类型数据
     * @param deviceTypeCode
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "读取设备类型数据", notes = "根据设备类型code读取设备数据")
    public ResponseEntity<?> deviceInfoLoad(String deviceTypeCode){
        try {
            return new ResponseEntity<>("",HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备类型读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到该设备记录",HttpStatus.CONFLICT);
        }
    }

    /**
     * TODO:读取设备类型列表
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<?> getDeviceTypeList(){
        try {
            return new ResponseEntity<>("",HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备类型读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到该设备记录",HttpStatus.CONFLICT);
        }
    }
}
