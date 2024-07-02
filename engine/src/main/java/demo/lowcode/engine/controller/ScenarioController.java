package demo.lowcode.engine.controller;

import demo.lowcode.engine.business.ScenarioBusiness;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ScenarioController {
    @Resource
    ScenarioBusiness scenarioBusiness;


    // load场景定义
    @PostMapping(value = "/load-scenario")
    public ResponseEntity<?> loadScenario(String scePath) {
        scenarioBusiness.loadScenario(scePath);
        return null;
    }
}
