package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceLibraryBusiness;
import demo.lowcode.platform.dto.DeviceMapperResult;
import demo.lowcode.platform.entity.DeviceLibrary;
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
@RequestMapping("/device-library")
@Api(value = "设备库接口", tags = { "设备库管理" })
public class DeviceLibraryController {

    @Resource
    private DeviceLibraryBusiness deviceLibraryBusiness;

    @GetMapping("/mapper")
    @ApiOperation("获取设备Mapper内容")
    public ResponseEntity<?> getMapper(@RequestParam String provider, @RequestParam String deviceModel) {
        try {
            DeviceMapperResult result = deviceLibraryBusiness.getMapperContent(provider, deviceModel);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/mapper")
    @ApiOperation("上传设备Mapper文件")
    public ResponseEntity<?> uploadMapper(@RequestParam("file") MultipartFile file,
            @RequestParam String provider,
            @RequestParam String deviceTypeName,
            @RequestParam String deviceModel) {
        return new ResponseEntity<>("暂未实现", HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/list")
    @ApiOperation("获取所有设备库列表")
    public ResponseEntity<?> list() {
        return new ResponseEntity<>(deviceLibraryBusiness.list(), HttpStatus.OK);
    }

    @GetMapping("/page")
    @ApiOperation("分页获取设备库列表")
    public ResponseEntity<?> page(@RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String deviceTypeName) {
        Page<DeviceLibrary> page = new Page<>(current, size);
        QueryWrapper<DeviceLibrary> queryWrapper = new QueryWrapper<>();
        if (provider != null && !provider.isEmpty()) {
            queryWrapper.like("provider", provider);
        }
        if (deviceTypeName != null && !deviceTypeName.isEmpty()) {
            queryWrapper.like("device_type_name", deviceTypeName);
        }
        queryWrapper.orderByDesc("create_time");
        return new ResponseEntity<>(deviceLibraryBusiness.page(page, queryWrapper), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取设备库详情")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        DeviceLibrary deviceLibrary = deviceLibraryBusiness.getById(id);
        if (deviceLibrary == null) {
            return new ResponseEntity<>("未找到该设备库信息", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deviceLibrary, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("添加设备库")
    public ResponseEntity<?> save(@RequestBody DeviceLibrary deviceLibrary) {
        boolean success = deviceLibraryBusiness.save(deviceLibrary);
        return success ? new ResponseEntity<>(deviceLibrary, HttpStatus.CREATED)
                : new ResponseEntity<>("添加失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    @ApiOperation("更新设备库")
    public ResponseEntity<?> update(@RequestBody DeviceLibrary deviceLibrary) {
        System.out.println("收到更新请求，设备库数据: " + deviceLibrary);
        System.out.println("设备库ID: " + deviceLibrary.getId());
        if (deviceLibrary.getId() == null) {
            System.err.println("错误：设备库ID为空，无法更新");
            return new ResponseEntity<>("更新失败：设备库ID为空", HttpStatus.BAD_REQUEST);
        }
        boolean success = deviceLibraryBusiness.updateById(deviceLibrary);
        System.out.println("更新结果: " + success);
        return success ? new ResponseEntity<>(deviceLibrary, HttpStatus.OK)
                : new ResponseEntity<>("更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除设备库")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean success = deviceLibraryBusiness.removeById(id);
        return success ? new ResponseEntity<>("删除成功", HttpStatus.OK)
                : new ResponseEntity<>("删除失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
