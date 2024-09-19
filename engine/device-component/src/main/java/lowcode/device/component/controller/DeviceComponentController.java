package lowcode.device.component.controller;

import demo.lowcode.common.Param;
import lowcode.device.component.business.DeviceComponentBusiness;


import lowcode.device.component.dto.CommandDto;
import lowcode.device.component.entity.Event ;
import demo.lowcode.common.Command;
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
import java.util.ArrayList;
import java.util.List;

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
            List<Param> paramList = deviceComponentBusiness.loadInputParam(deviceName,commandCode);
            return new ResponseEntity<>(paramList, HttpStatus.OK);
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
            List<Event> eventList= deviceComponentBusiness.loadEvent(deviceName,operationCode);
            return new ResponseEntity<List<Event>>(eventList, HttpStatus.OK);
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
    public ResponseEntity<?> loadService(String deviceName,String serviceName) {
        try {
            BrandService brandService= deviceComponentBusiness.loadService(deviceName,serviceName);
            List<BrandService> brandServiceList = new ArrayList<>();
            brandServiceList.add(brandService);
            return new ResponseEntity<>(brandServiceList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }
}
