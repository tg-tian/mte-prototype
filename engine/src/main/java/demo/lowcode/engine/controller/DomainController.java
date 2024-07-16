package demo.lowcode.engine.controller;

import demo.lowcode.engine.business.DomainBusiness;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
public class DomainController {
    @Resource
    DomainBusiness domainBusiness;

    // load领域定义
    // TODO: 解释引擎不用加载domain。应该放到开发工具中。但是Domain这个概念还是可以在engine中保留。
    @PostMapping(value = "/load-domain")
    public ResponseEntity<?> loadDomain(String domainPath) {
        try {
            domainBusiness.loadDomain(domainPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
