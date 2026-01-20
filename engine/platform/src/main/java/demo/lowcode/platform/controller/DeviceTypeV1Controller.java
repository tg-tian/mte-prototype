package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceTypeV1Business;
import demo.lowcode.platform.entity.DeviceTypeV1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device-types")
@CrossOrigin
@Api(value = "设备类型V1接口", tags = { "设备类型V1管理" })
public class DeviceTypeV1Controller {

  @Autowired
  private DeviceTypeV1Business deviceTypeV1Business;

  @GetMapping
  @ApiOperation("查询设备类型列表")
  public ResponseEntity<List<DeviceTypeV1>> list() {
    try {
      return new ResponseEntity<>(deviceTypeV1Business.list(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  @ApiOperation("根据ID查询设备类型")
  public ResponseEntity<DeviceTypeV1> getById(@PathVariable Long id) {
    try {
      DeviceTypeV1 deviceTypeV1 = deviceTypeV1Business.getById(id);
      if (deviceTypeV1 == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(deviceTypeV1, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  @ApiOperation("新增设备类型")
  public ResponseEntity<?> save(@RequestBody DeviceTypeV1 deviceTypeV1) {
    try {
      DeviceTypeV1 savedDevice = deviceTypeV1Business.save(deviceTypeV1);
      return new ResponseEntity<>(savedDevice, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{id}")
  @ApiOperation("更新设备类型")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DeviceTypeV1 deviceTypeV1) {
    try {
      deviceTypeV1.setId(id);
      DeviceTypeV1 updatedDevice = deviceTypeV1Business.update(deviceTypeV1);
      return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  @ApiOperation("删除设备类型")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
      boolean deleted = deviceTypeV1Business.delete(id);
      if (deleted) {
        return new ResponseEntity<>("删除成功", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("未找到该设备类型", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
