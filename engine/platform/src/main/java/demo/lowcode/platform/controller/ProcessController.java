package demo.lowcode.platform.controller;

import demo.lowcode.common.Param;
import demo.lowcode.platform.business.ProcessBusiness;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ProcessController {
    @Resource
    ProcessBusiness processBusiness;

    @GetMapping(value = "/get-process-config")
    public ResponseEntity<?> getProcessConfig(@RequestParam String processId) {
        Map<String, List<Param>> result = processBusiness.getProcessConfig(processId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 运行流程
    //TODO: 解释引擎支持多个app：参数要加上appId
    @PostMapping(value = "/execute-process")
    public ResponseEntity<?> executeProcess(@RequestParam String processId, @RequestBody Map<String, Map<String, Object>> requestBody) {
        processBusiness.executeProcess(processId, requestBody);
        return new ResponseEntity<>("结束执行", HttpStatus.OK);
    }
}
