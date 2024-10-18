package com.mock.device.controller;

import com.mock.device.business.MockBusiness;
import com.mock.device.dto.MockDeviceInformation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class MockController {
    @Resource
    MockBusiness mockBusiness;

    // 获取咖啡机器人A状态
    @GetMapping("/mock-device-information/a")
    public ResponseEntity<MockDeviceInformation> getMockDeviceInformationA() {
        MockDeviceInformation mockDeviceInformation = mockBusiness.getAMockData();
        return new ResponseEntity<>(mockDeviceInformation, HttpStatus.OK);
    }

    // 获取咖啡机器人B状态
    @GetMapping("/mock-device-information/b")
    public ResponseEntity<MockDeviceInformation> getMockDeviceInformationB() {
        MockDeviceInformation mockDeviceInformation = mockBusiness.getBMockData();
        return new ResponseEntity<>(mockDeviceInformation, HttpStatus.OK);
    }

    @PostMapping("/coffeemaker/aservice")
    public ResponseEntity<?> executeOperationA(@RequestParam String operation, @RequestBody(required = false) Map<String, String> params) {
        try {
            mockBusiness.executeOperationA(operation, params);
            return new ResponseEntity<>("执行成功", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("执行失败："+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/coffeemaker/bservice")
    public ResponseEntity<?> executeOperationB(@RequestParam String operation, @RequestBody(required = false) Map<String, String> params) {
        try {
            mockBusiness.executeOperationB(operation, params);
            return new ResponseEntity<>("执行成功", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("执行失败："+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
