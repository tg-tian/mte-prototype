package demo.lowcode.engine.controller;

import demo.lowcode.engine.business.ApplicationBusiness;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {
    @Resource
    ApplicationBusiness applicationBusiness;

    // load应用定义
    @PostMapping(value = "/load-application")
    public ResponseEntity<?> loadScenario(String appPath) {
        applicationBusiness.loadApplication(appPath);
        return null;
    }
}
