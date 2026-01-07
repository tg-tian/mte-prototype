package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceLibraryBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.locks.Lock;

@RestController
@RequestMapping("/device-library")
@Api(value = "设备库接口", tags = {"设备库管理"})
public class DeviceLibraryController {

    @Resource
    private DeviceLibraryBusiness deviceLibraryBusiness;

    @GetMapping("/mapper")
    public ResponseEntity<?> getMapper(@RequestParam String provider,
                                       @RequestParam String category,
                                       @RequestParam String deviceModel) {
        try {
            String content = deviceLibraryBusiness.getMapperContent(provider, category, deviceModel);
            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/mapper")
    public ResponseEntity<?> uploadMapper(@RequestParam("file") MultipartFile file,
                                          @RequestParam String provider,
                                          @RequestParam String category,
                                          @RequestParam String deviceModel) {
        return new ResponseEntity<>("暂未实现", HttpStatus.NOT_IMPLEMENTED);
    }
}
