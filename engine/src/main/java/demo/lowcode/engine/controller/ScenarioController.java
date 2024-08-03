package demo.lowcode.engine.controller;

import demo.lowcode.engine.business.ScenarioBusiness;
import demo.lowcode.engine.model.ScenarioJson;
import demo.lowcode.engine.model.Scenario_ResourceJson;
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

    @GetMapping (value = "/load-scenario-json")
    public ResponseEntity<?> loadScenarioJson(){
        try {
            ScenarioJson scenarioJson =  scenarioBusiness.loadScenarioJson();
            System.out.println("场景数据loading成功！");
            return new ResponseEntity<>(scenarioJson, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("场景数据读取错误",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping (value = "/load-scenario-resource-json")
    public ResponseEntity<?> loadResourceJson(){
        try {
            Scenario_ResourceJson scenario_resourceJson = scenarioBusiness.loadResourceJson();
            System.out.println("场景资源loading成功！");
            return new ResponseEntity<>(scenario_resourceJson, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("场景资源读取错误",HttpStatus.BAD_REQUEST);
        }
    }
}
