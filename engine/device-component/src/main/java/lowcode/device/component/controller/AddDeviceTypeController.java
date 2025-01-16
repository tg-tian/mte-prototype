package lowcode.device.component.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import lowcode.device.component.business.AddDeviceTypeBusiness;
import lowcode.device.component.dto.ParamDto;
import lowcode.device.component.dto.request.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@ApiModel(value = "新增设备类型")
public class AddDeviceTypeController {
    @Resource
    AddDeviceTypeBusiness addDeviceTypeBusiness;

    @ApiOperation(value = "初始化设备主文件")
    @PostMapping(value = "/add-device-type/init")
    public ResponseEntity<?> initDeviceType(@RequestBody InitDeviceTypeRqt request) {
        try {
            addDeviceTypeBusiness.initDeviceType(request.getDeviceCode(), request.getDeviceName(), request.getImageUrl());
            return new ResponseEntity<>("初始化文件成功", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("初始化错误:"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "新增属性")
    @PostMapping(value = "/add-device-type/property/add")
    public ResponseEntity<?> addDeviceTypeProperty(@RequestBody AddPropertyRqt request) {
        try {
            addDeviceTypeBusiness.addProperty(request.getDeviceType(),request.getProperties());
            return new ResponseEntity<>("新增属性成功", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("新增属性错误:"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "新增操作")
    @PostMapping(value = "/add-device-type/operation/add")
    public ResponseEntity<?> addDeviceTypeOperation(@RequestBody AddOperationRqt request) {
        try {
            addDeviceTypeBusiness.addCommand(request.getDeviceType(), request.getCommand());
            return new ResponseEntity<>("新增操作成功", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("新增操作错误:"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "新增操作事件")
    @PostMapping(value = "/add-device-type/event/add")
    public ResponseEntity<?> addDeviceTypeEvent(@RequestBody AddEventRqt request) {
        try {
            addDeviceTypeBusiness.addEvent(request.getDeviceType(), request.getCommandCode(), request.getDeviceEvent());
            return new ResponseEntity<>("新增操作成功", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("新增操作错误:"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "初始化品牌服务文件")
    @PostMapping(value = "/add-device-type/service/init")
    public ResponseEntity<?> initDeviceTypeService(@RequestBody InitServiceRqt request) {
        try {
            addDeviceTypeBusiness.initService(request.getDeviceType(), request.getBrandService());
            return new ResponseEntity<>("新增品牌厂商成功", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("新增品牌错误:"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "编辑品牌服务文件")
    @PostMapping(value = "/add-device-type/service/edit")
    public ResponseEntity<?> initDeviceTypeService(@RequestBody EditServiceRqt request) {
        try {
            addDeviceTypeBusiness.editService(request.getDeviceType(), request.getServiceName());
            return new ResponseEntity<>("编辑品牌厂商成功", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("编辑品牌错误:"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
