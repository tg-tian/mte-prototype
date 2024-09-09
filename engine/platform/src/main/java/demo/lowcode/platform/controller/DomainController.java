package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DomainBusiness;
import demo.lowcode.platform.dto.DomainJson;
import demo.lowcode.platform.dto.Domain_ComponentJson;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

    // load领域json文件
    @GetMapping(value = "/load-domain-json")
    public ResponseEntity<?> loadDomainJson() {
        try {
            DomainJson domainJson = domainBusiness.loadJson();
            return new ResponseEntity<>(domainJson, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/load-domain-component-json")
    public ResponseEntity<?> loadDomainComponentJson(String componentType) {
        try {
            Domain_ComponentJson componentJson = domainBusiness.loadComponentJson(componentType);
            return new ResponseEntity<>(componentJson, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }
}
