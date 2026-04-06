package demo.lowcode.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.lowcode.platform.business.DeviceModelBusiness;
import demo.lowcode.platform.entity.DeviceModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meta/device-models")
@CrossOrigin
@Api(value = "设备模型接口", tags = { "设备模型管理" })
public class DeviceModelController {

  @Autowired
  private DeviceModelBusiness deviceModelBusiness;

  @GetMapping
  @ApiOperation("查询设备模型列表")
  public ResponseEntity<List<DeviceModel>> list() {
    try {
      return new ResponseEntity<>(deviceModelBusiness.list(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/page")
  @ApiOperation("分页查询设备模型列表")
  public ResponseEntity<?> page(
      @RequestParam(defaultValue = "1") Integer current,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(required = false) String modelName,
      @RequestParam(required = false) String category) {
    try {
      Page<DeviceModel> page = new Page<>(current, size);
      QueryWrapper<DeviceModel> queryWrapper = new QueryWrapper<>();
      if (modelName != null && !modelName.isEmpty()) {
        queryWrapper.like("modelName", modelName);
      }
      if (category != null && !category.isEmpty()) {
        queryWrapper.eq("category", category);
      }
      queryWrapper.orderByDesc("create_time");
      return new ResponseEntity<>(deviceModelBusiness.page(page, queryWrapper), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{idOrModelId}")
  @ApiOperation("根据数据库ID或模型ID查询设备模型")
  public ResponseEntity<DeviceModel> getByIdOrModelId(@PathVariable String idOrModelId) {
    try {
      DeviceModel deviceModel;
      if (idOrModelId.matches("^\\d+$")) {
        deviceModel = deviceModelBusiness.getById(Long.valueOf(idOrModelId));
        if (deviceModel == null) {
          deviceModel = deviceModelBusiness.getByModelId(idOrModelId);
        }
      } else {
        deviceModel = deviceModelBusiness.getByModelId(idOrModelId);
      }

      if (deviceModel == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(deviceModel, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  @ApiOperation("新增设备模型")
  public ResponseEntity<?> save(@RequestBody DeviceModel deviceModel) {
    try {
      DeviceModel savedDevice = deviceModelBusiness.save(deviceModel);
      return new ResponseEntity<>(savedDevice, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{id}")
  @ApiOperation("更新设备模型")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DeviceModel deviceModel) {
    try {
      deviceModel.setId(id);
      DeviceModel updatedDevice = deviceModelBusiness.update(deviceModel);
      return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  @ApiOperation("删除设备模型")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
      boolean deleted = deviceModelBusiness.delete(id);
      if (deleted) {
        return new ResponseEntity<>("删除成功", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("未找到该设备模型", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}



