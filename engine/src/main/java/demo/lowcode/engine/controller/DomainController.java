package demo.lowcode.engine.controller;

import demo.lowcode.engine.business.DomainBusiness;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Service
public class DomainController {
    @Resource
    DomainBusiness domainBusiness;

    // load领域定义
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
