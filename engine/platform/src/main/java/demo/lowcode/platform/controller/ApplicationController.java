package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.ApplicationBusiness;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ApplicationController {
    @Resource
    ApplicationBusiness applicationBusiness;

    // load应用定义
    // TODO: 解释引擎：可能要加载多个app。不同app应该有不同的入口
    @PostMapping(value = "/load-application")
    public ResponseEntity<?> loadScenario(String appPath) {
        applicationBusiness.loadApplication(appPath);
        return null;
    }
}
