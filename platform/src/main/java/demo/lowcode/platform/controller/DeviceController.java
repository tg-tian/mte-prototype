package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceBusiness;
import demo.lowcode.platform.dto.NewDevice;
import demo.lowcode.platform.dto.NewDeviceType;
import demo.lowcode.platform.entity.Device;
import demo.lowcode.platform.entity.DeviceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Api(value = "设备接口",tags={"设备实例管理"})
public class DeviceController {
    @Resource
    private DeviceBusiness deviceBusiness;

    @GetMapping("/devices")
    @ApiOperation(value = "获取当前场景的设备列表")
    public ResponseEntity<?> getDevices(@RequestParam Long sceneId){
        try {
            List<Device> deviceList = deviceBusiness.getDeviceListByScene(sceneId);
            return new ResponseEntity<>(deviceList, HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到设备列表",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/devices/devicetype-list")
    @ApiOperation(value = "获取当前场景可添加的设备类型列表")
    public ResponseEntity<?> getDeviceTypesByScene(@RequestParam Long sceneId){
        try {
            List<DeviceType> deviceTypeList = deviceBusiness.getDeviceTypeListByScene(sceneId);
            return new ResponseEntity<>(deviceTypeList, HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备类型读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到设备类型列表",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/devices")
    @ApiOperation(value = "添加设备")
    public ResponseEntity<?> createDevice(@RequestBody NewDevice newDevice){
        try {
            Device device=deviceBusiness.createDevice(newDevice);
            return new ResponseEntity<>(device,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/devices/{id}")
    @ApiOperation(value = "修改设备信息")
    public ResponseEntity<?> updateDevice(@PathVariable("id") Long id, @RequestBody NewDevice newDevice){
        try {
            Device device = deviceBusiness.updateDevice(id, newDevice);
            return new ResponseEntity<>(device,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/devices/{id}")
    @ApiOperation(value = "删除设备")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Long id){
        try {
            deviceBusiness.deleteDeviceByID(id);
            return new ResponseEntity<>("删除成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/devices/connections")
    @ApiOperation(value = "获取当前场景的设备连接列表")
    public ResponseEntity<?> getDeviceConnectionsByScene(@RequestParam Long sceneId){
        try {
            List<NewDevice> deviceConnections = deviceBusiness.getDeviceConnectionsListByScene(sceneId);
            return new ResponseEntity<>(deviceConnections, HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备连接读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到设备连接列表",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/devices/add-connection")
    @ApiOperation(value = "增加当前场景的设备连接列表")
    public ResponseEntity<?> addDeviceConnection(@RequestParam Long sourceId,@RequestParam Long targetId,@RequestParam String position){
        try {
            deviceBusiness.addConnection(sourceId,targetId,position);
            return new ResponseEntity<>("增加连接成功", HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备连接读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到设备连接列表",HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/devices/delete-connection")
    @ApiOperation(value = "删除当前场景的设备连接列表")
    public ResponseEntity<?> deleteDeviceConnection(@RequestParam Long sourceId,@RequestParam Long targetId){
        try {
            deviceBusiness.deleteConnection(sourceId,targetId);
            return new ResponseEntity<>("删除成功", HttpStatus.OK);
        }catch (RuntimeException e){
            System.err.println("设备连接读取失败: " + e.getMessage());
            return new ResponseEntity<>("未查询到设备连接列表",HttpStatus.NOT_FOUND);
        }
    }
}
