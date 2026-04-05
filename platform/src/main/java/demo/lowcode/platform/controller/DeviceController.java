package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceBusiness;
import demo.lowcode.platform.dto.DeviceMapperResult;
import demo.lowcode.platform.entity.Device;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/device")
@Api(value = "设备接口", tags = { "设备管理" })
public class DeviceController {

    @Resource
    private DeviceBusiness deviceBusiness;

    @GetMapping("/mapper")
    @ApiOperation("获取设备Mapper内容")
    public ResponseEntity<?> getMapper(@RequestParam String provider, @RequestParam String deviceId) {
        try {
            DeviceMapperResult result = deviceBusiness.getMapperContent(provider, deviceId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/mapper/{id}/generate")
    @ApiOperation("根据现有配置重新生成设备的 Mapper 文件")
    public ResponseEntity<?> generateMapper(@PathVariable Long id) {
        try {
            deviceBusiness.generateMapper(id);
            return new ResponseEntity<>("生成成功", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/mapper")
    @ApiOperation("上传设备Mapper文件")
    public ResponseEntity<?> uploadMapper(@RequestParam("file") MultipartFile file,
            @RequestParam String provider,
            @RequestParam String deviceName,
            @RequestParam String deviceId) {
        return new ResponseEntity<>("暂未实现", HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/list")
    @ApiOperation("获取所有设备列表")
    public ResponseEntity<?> list() {
        return new ResponseEntity<>(deviceBusiness.list(), HttpStatus.OK);
    }

    @GetMapping("/page")
    @ApiOperation("分页获取设备列表")
    public ResponseEntity<?> page(@RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String deviceName) {
        Page<Device> page = new Page<>(current, size);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        if (provider != null && !provider.isEmpty()) {
            queryWrapper.like("provider", provider);
        }
        if (deviceName != null && !deviceName.isEmpty()) {
            queryWrapper.like("device_name", deviceName);
        }
        queryWrapper.orderByDesc("create_time");
        return new ResponseEntity<>(deviceBusiness.page(page, queryWrapper), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取设备详情")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Device device = deviceBusiness.getById(id);
        if (device == null) {
            return new ResponseEntity<>("未找到该设备信息", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("添加设备")
    public ResponseEntity<?> save(@RequestBody Device device) {
        boolean success = deviceBusiness.save(device);
        return success ? new ResponseEntity<>(device, HttpStatus.CREATED)
                : new ResponseEntity<>("添加失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    @ApiOperation("更新设备")
    public ResponseEntity<?> update(@RequestBody Device device) {
        if (device.getId() == null) {
            return new ResponseEntity<>("更新失败：设备ID为空", HttpStatus.BAD_REQUEST);
        }
        boolean success = deviceBusiness.updateById(device);
        return success ? new ResponseEntity<>(device, HttpStatus.OK)
                : new ResponseEntity<>("更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除设备")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean success = deviceBusiness.removeById(id);
        return success ? new ResponseEntity<>("删除成功", HttpStatus.OK)
                : new ResponseEntity<>("删除失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


