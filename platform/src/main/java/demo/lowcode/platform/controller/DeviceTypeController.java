package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceTypeBusiness;
import demo.lowcode.platform.dto.BindInfo;
import demo.lowcode.platform.dto.ModelInfo;
import demo.lowcode.platform.dto.NewDeviceType;
import demo.lowcode.platform.entity.DeviceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
@Api(value = "设备类型接口",tags={"设备类型管理"})
public class DeviceTypeController {
    @Resource
    private DeviceTypeBusiness deviceTypeBusiness;

    /**
     * TODO:同步iot平台设备类型
     * @param deviceType
     * @return
     */
    @PostMapping("/device-type/upload")  //应该改成/info
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
    @GetMapping("/device-type/info")
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
    @GetMapping("/device-type/list")
    public ResponseEntity<?> getDeviceTypeList(){
        try {
            return new ResponseEntity<>("",HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备类型读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到该设备记录",HttpStatus.CONFLICT);
        }
    }

//    ------------------------------v2.0--------------------------------------------
    @GetMapping("/devicetypes")
    @ApiOperation(value = "获取设备类型列表/获取当前领域绑定的设备类型列表")
    public ResponseEntity<?> getDeviceTypes(@RequestParam(required = false) Long domainId){
        try {
            List<DeviceType> deviceTypeList;
            if (domainId!=null){
                deviceTypeList=deviceTypeBusiness.getDeviceTypeListByDomain(domainId);
            }else{
                deviceTypeList=deviceTypeBusiness.getDeviceTypeList();
            }
            return new ResponseEntity<>(deviceTypeList,HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备类型读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到设备类型列表",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/domain/devicetype/binding")
    @ApiOperation(value = "领域绑定设备组件")
    public ResponseEntity<?> bindDeviceType(@RequestBody BindInfo bindInfo){
        try {
            deviceTypeBusiness.bindDomainAndDeviceType(bindInfo.getDomainId(), bindInfo.getDeviceTypeId());
            return new ResponseEntity<>("绑定成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/domain/devicetype/unbinding")
    @ApiOperation(value = "领域取消绑定设备组件")
    public ResponseEntity<?> unbindDeviceType(@RequestBody BindInfo bindInfo){
        try {
            deviceTypeBusiness.unbindDomainAndDeviceType(bindInfo.getDomainId(), bindInfo.getDeviceTypeId());
            return new ResponseEntity<>("取消绑定成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/devicetypes/{id}")
    @ApiOperation(value = "根据id获取设备类型")
    public ResponseEntity<?> getDeviceTypeById(@PathVariable Long id) {
        try {
            DeviceType deviceType=deviceTypeBusiness.getDeviceTypeById(id);
            return new ResponseEntity<>(deviceType,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/devicetypes")
    @ApiOperation(value = "创建设备类型")
    public ResponseEntity<?> createDeviceType(@RequestBody NewDeviceType newDeviceType){
        try {
            DeviceType deviceType = deviceTypeBusiness.createDeviceType(
                newDeviceType.getCode(),
                newDeviceType.getName(),
                newDeviceType.getDescription(),
                newDeviceType.getModel()
            );
            return new ResponseEntity<>(deviceType, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/devicetypes/{id}")
    @ApiOperation(value = "修改设备类型基本信息")
    public ResponseEntity<?> updateDeviceType(@PathVariable("id") Long id, @RequestBody NewDeviceType newDeviceType){
        try {
            DeviceType deviceType=deviceTypeBusiness.updateDeviceType(id, newDeviceType);
            return new ResponseEntity<>(deviceType,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/devicetypes/{id}")
    @ApiOperation(value = "删除设备类型")
    public ResponseEntity<?> deleteDeviceType(@PathVariable("id") Long id){
        try {
            deviceTypeBusiness.deleteDeviceTypeByID(id);
            return new ResponseEntity<>("删除成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/devicetypes/model")
    @ApiOperation(value = "添加/更新设备类型模型")
    public ResponseEntity<?> addDeviceTypeModel(@RequestBody ModelInfo modelInfo){
        try {
            DeviceType deviceType=deviceTypeBusiness.updateModel(modelInfo.getDeviceTypeId(), modelInfo.getModel());
            return new ResponseEntity<>(deviceType,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
