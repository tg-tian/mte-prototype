package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.TemplateBusiness;
import demo.lowcode.platform.dto.TemplateBindInfo;
import demo.lowcode.platform.dto.TemplateUnbindInfo;
import demo.lowcode.platform.entity.Template;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TemplateController {
    @Resource
    TemplateBusiness templateBusiness;

    @GetMapping("/templates/domain")
    @ApiOperation(value = "获取领域已绑定的模板列表")
    public ResponseEntity<?> getTemplates(@RequestParam Long domainId){
        try {
            List<Template> templateList = templateBusiness.getTemplateList(domainId);
            return new ResponseEntity<>(templateList, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("未查询到模板列表",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/templates/binding")
    @ApiOperation(value = "领域绑定模板")
    public ResponseEntity<?> bindTemplate(@RequestBody TemplateBindInfo bindInfo){
        try {
            templateBusiness.bindDomainAndTemplate(bindInfo.getDomainId(), bindInfo.getTemplateId());
            return new ResponseEntity<>("绑定成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/templates/unbinding")
    @ApiOperation(value = "领域取消绑定模板")
    public ResponseEntity<?> unbindDeviceType(@RequestBody TemplateUnbindInfo unbindInfo){
        try {
            templateBusiness.unbindDomainAndTemplate(unbindInfo.getDomainId(), unbindInfo.getTemplateId());
            return new ResponseEntity<>("取消绑定成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
