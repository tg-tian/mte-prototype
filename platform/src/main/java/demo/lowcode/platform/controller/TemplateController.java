package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.TemplateBusiness;
import demo.lowcode.platform.dto.TemplateBindInfo;
import demo.lowcode.platform.dto.TemplateImportInfo;
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
    public ResponseEntity<?> getTemplates(@RequestParam String domainCode){
        try {
            List<Template> templateList = templateBusiness.getTemplateList(domainCode);
            return new ResponseEntity<>(templateList, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("未查询到模板列表",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/templates")
    @ApiOperation(value = "获取所有模板列表")
    public ResponseEntity<?> getAllTemplates(){
        try {
            List<Template> templateList = templateBusiness.getAllTemplates();
            return new ResponseEntity<>(templateList, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("未查询到模板列表",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/templates/binding")
    @ApiOperation(value = "领域绑定模板")
    public ResponseEntity<?> bindTemplate(@RequestBody TemplateBindInfo bindInfo){
        try {
            templateBusiness.bindDomainAndTemplate(bindInfo.getDomainCode(), bindInfo.getTemplateId());
            return new ResponseEntity<>("绑定成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/templates/unbinding")
    @ApiOperation(value = "领域取消绑定模板")
    public ResponseEntity<?> unbindDeviceType(@RequestBody TemplateUnbindInfo unbindInfo){
        try {
            templateBusiness.unbindDomainAndTemplate(unbindInfo.getDomainCode(), unbindInfo.getTemplateId());
            return new ResponseEntity<>("取消绑定成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/templates/import")
    @ApiOperation(value = "从外部模板库导入模板")
    public ResponseEntity<?> importTemplate(@RequestBody TemplateImportInfo importInfo){
        try {
            Template template = templateBusiness.importTemplate(importInfo.getTemplateId());
            return new ResponseEntity<>(template, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/templates/{templateId}")
    @ApiOperation(value = "获取单个模板")
    public ResponseEntity<?> getTemplateById(@PathVariable Long templateId){
        try {
            Template template = templateBusiness.getTemplateByTemplateId(templateId);
            return new ResponseEntity<>(template, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/templates")
    @ApiOperation(value = "创建模板")
    public ResponseEntity<?> createTemplate(@RequestBody Template template){
        try {
            Template created = templateBusiness.createTemplate(template);
            return new ResponseEntity<>(created, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/templates/{templateId}")
    @ApiOperation(value = "更新模板")
    public ResponseEntity<?> updateTemplate(@PathVariable Long templateId, @RequestBody Template template){
        try {
            Template updated = templateBusiness.updateTemplateByTemplateId(templateId, template);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/templates/{templateId}")
    @ApiOperation(value = "删除模板")
    public ResponseEntity<?> deleteTemplate(@PathVariable Long templateId){
        try {
            templateBusiness.deleteTemplateByTemplateId(templateId);
            return new ResponseEntity<>("删除成功", HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}