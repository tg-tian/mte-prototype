package demo.lowcode.engine.controller;

import demo.lowcode.engine.business.ProcessBusiness;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public class ProcessController {
    @Resource
    ProcessBusiness processBusiness;

    // load流程定义
    @PostMapping(value = "/load-process")
    public ResponseEntity<?> loadProcess(String proPath) {
        try {
            processBusiness.loadProcess(proPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 运行流程
    @PostMapping(value = "/execute-process")
    public ResponseEntity<?> executeProcess(String processId) {
        processBusiness.executeProcess(processId);
        return null;
    }
}
