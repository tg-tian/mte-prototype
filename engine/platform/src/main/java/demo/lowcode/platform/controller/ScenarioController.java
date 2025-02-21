package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.ScenarioBusiness;
import demo.lowcode.platform.model.ScenarioJson;
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
}
