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
    // TODO: 解释引擎不用加载scenario。应该放到开发工具中。但是scenario这个概念还是可以在engine中保留。
    @PostMapping(value = "/load-scenario")
    public ResponseEntity<?> loadScenario(String scePath) {
        scenarioBusiness.loadScenario(scePath);
        return null;
    }
}
