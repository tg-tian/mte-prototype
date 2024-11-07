package lowcode.device.component.controller;

import lowcode.device.component.business.DeviceComponentBusiness;


import lowcode.device.component.dto.CommandDto;
import lowcode.device.component.dto.ParamDto;
import lowcode.device.component.entity.DeviceEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lowcode.device.component.entity.BrandService ;
import jakarta.annotation.Resource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController //?
@CrossOrigin    //?
@ApiModel(value = "设备组件拓展类")
public class DeviceComponentController {
    @Resource
    DeviceComponentBusiness deviceComponentBusiness;
    @ApiOperation(value = "操作参数读取接口")
    @GetMapping(value = "/load-operation-param")
    public ResponseEntity<?> loadOperationParam(String deviceName, String commandCode) {
        try {
            ParamDto paramDto = deviceComponentBusiness.loadOperationParam(deviceName,commandCode);
            return new ResponseEntity<>(paramDto, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println("参数信息加载错误");
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "操作事件读取接口")
    @GetMapping(value = "/load-operation-event")
    public ResponseEntity<?> loadOperationEvent(String deviceName,String operationCode) {
        try {
            List<DeviceEvent> deviceEventList;
            if (Objects.equals(operationCode, "")){
                deviceEventList = deviceComponentBusiness.loadEvent(deviceName);
            }else {
                deviceEventList = deviceComponentBusiness.loadEvent(deviceName,operationCode);
            }
            return new ResponseEntity<List<DeviceEvent>>(deviceEventList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "操作信息读取接口")
    @GetMapping(value = "/load-operation-command")
    public ResponseEntity<?> loadOperationCommand(String deviceType) {
        try {
            System.out.println(deviceType);
            List<CommandDto> commandList= deviceComponentBusiness.loadCommand(deviceType);
            return new ResponseEntity<>(commandList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "服务读取接口")
    @GetMapping(value = "/load-service")
    public ResponseEntity<?> loadService(String deviceName) {
        try {
            List<BrandService> brandServiceList= deviceComponentBusiness.loadServiceList(deviceName);
            return new ResponseEntity<>(brandServiceList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }
}
