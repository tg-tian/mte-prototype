package demo.lowcode.engine.controller;

import demo.lowcode.common.Param;
import demo.lowcode.engine.business.DeviceComponentBusiness;


import demo.lowcode.engine.entity.Event;
import demo.lowcode.common.Command;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import demo.lowcode.engine.entity.BrandService;
import jakarta.annotation.Resource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
            System.out.println(deviceName+commandCode);
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
    public ResponseEntity<?> loadOperationEvent(String eventPath) {
        try {
            System.out.println(eventPath);
            List<Event> eventList= deviceComponentBusiness.loadEvent(eventPath);
            return new ResponseEntity<>(eventList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "操作信息读取接口")
    @GetMapping(value = "/load-operation-command")
    public ResponseEntity<?> loadOperationCommand(String devicePath) {
        try {
            System.out.println(devicePath);
            List<Command> commandList= deviceComponentBusiness.loadCommand(devicePath);
            return new ResponseEntity<>(commandList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "服务读取接口")
    @GetMapping(value = "/load-service")
    public ResponseEntity<?> loadService(String servicePath) {
        try {
            System.out.println(servicePath);
            BrandService brandService= deviceComponentBusiness.loadService(servicePath);
            return new ResponseEntity<>(brandService, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }
}
