package lowcode.device.component.controller;

import lowcode.device.component.business.MockBusiness;
import lowcode.device.component.dto.MockDeviceInformation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MockController {
    @Resource
    MockBusiness mockBusiness;

    @GetMapping("/mock-device-information")
    public ResponseEntity<MockDeviceInformation> getMockDeviceInformation() {
        MockDeviceInformation mockDeviceInformation = mockBusiness.getMockData();
        return new ResponseEntity<>(mockDeviceInformation, HttpStatus.OK);
    }
}
