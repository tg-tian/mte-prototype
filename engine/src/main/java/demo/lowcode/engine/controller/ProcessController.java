package demo.lowcode.engine.controller;

import demo.lowcode.engine.business.ProcessBusiness;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ProcessController {
    @Resource
    ProcessBusiness processBusiness;

    // 运行流程
    @PostMapping(value = "/execute-process")
    public ResponseEntity<?> executeProcess(@RequestParam String processId) {
        processBusiness.executeProcess(processId);
        return new ResponseEntity<>("结束执行", HttpStatus.OK);
    }
}
