package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DomainBusiness;
import demo.lowcode.platform.dto.*;
import demo.lowcode.platform.entity.Domain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@Api(value = "领域接口",tags={"领域内容管理"})
public class DomainController {
    @Resource
    DomainBusiness domainBusiness;

    // load领域json文件
    @GetMapping(value = "/load-domain-json")
    public ResponseEntity<?> loadDomainJson(@RequestParam(defaultValue = "SmartBuilding") String domainCode) {
        try {
            DomainJson domainJson = domainBusiness.loadJson(domainCode);
            return new ResponseEntity<>(domainJson, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/load-domain-component-json")
    public ResponseEntity<?> loadDomainComponentJson(@RequestParam String componentType, @RequestParam(defaultValue = "SmartBuilding") String domainCode) {
        try {
            Domain_ComponentJson componentJson = domainBusiness.loadComponentJson(componentType, domainCode);
            return new ResponseEntity<>(componentJson, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * TODO:读取相应组件的组件列表
     * @param componentType
     * @return
     */
    @GetMapping(value = "/load-domain-component-data")
    public ResponseEntity<?> loadDomainComponentData(String componentType) {
        return new ResponseEntity<>("",HttpStatus.OK);
    }

    @GetMapping(value = "/domains")
    public ResponseEntity<?> getDomainList(){
        List<Domain> response = domainBusiness.getDomainList();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(value = "/domains/{id}")
    public ResponseEntity<?> getDomainListById(@PathVariable Long id){
        Domain response = domainBusiness.getDomainByID(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(value = "/domains")
    public ResponseEntity<?> createDomain(@RequestBody NewDomain newDomain){
        try {
            Domain domain = domainBusiness.createDomain(newDomain);
            return new ResponseEntity<>(domain ,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("创建失败",HttpStatus.CONFLICT);
        }
    }

    @PutMapping (value = "/domains/{id}")
    public ResponseEntity<?> changeDomain(@PathVariable Long id ,@RequestBody NewDomain newDomain){
        try {
            Domain domain  = domainBusiness.changeDomainByID(id,newDomain);
            return new ResponseEntity<>(domain,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("修改失败",HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "/domains/{id}")
    public ResponseEntity<?> deleteDomain(@PathVariable Long id){
        try {
            domainBusiness.deleteDomainByID(id);
            return new ResponseEntity<>("删除成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("删除失败",HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/domains/publish")
    @ApiOperation(value = "发布/取消发布领域")
    public ResponseEntity<?> publishDomain(@RequestBody Long domainId){
        try {
            Object result = domainBusiness.publishDomain(domainId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("操作失败：" + e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/domains/download/{id}")
    @ApiOperation(value = "下载领域配置文件")
    public ResponseEntity<?> downloadDomain(@PathVariable Long id){
        try {
            org.springframework.core.io.Resource domain = domainBusiness.downloadDomain(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + domain.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(domain);
        }catch (RuntimeException e){
            return new ResponseEntity<>("文件下载失败",HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/domains/templates")
    @ApiOperation(value = "新增领域模版")
    public ResponseEntity<?> createDomainTemplate(@RequestBody DomainTemInfo domainTemInfo ){
        try {
            domainBusiness.writeDomainInfo(domainTemInfo);
            return new ResponseEntity<>("保存成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("保存失败",HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/domains/templates")
    @ApiOperation(value = "获取领域模版列表")
    public ResponseEntity<?> getDomainTemplate(){
        try {
            List<DomainTemInfo> domainTemInfoList = domainBusiness.getDomainTemplates();
            return new ResponseEntity<>(domainTemInfoList,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("领域模版获取失败",HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/domains/templateId")
    @ApiOperation(value = "新增领域模版,存储领域模板在模板库中id")
    public ResponseEntity<?> updateDomainTemplate(@RequestBody DomainTemplateInfo domainTemInfo ){
        try {
            domainBusiness.updateDomainTemplate(domainTemInfo);
            return new ResponseEntity<>("保存成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("保存失败",HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/domains/from-template")
    @ApiOperation(value = "从模板新增领域")
    public ResponseEntity<?> createDomainFromTemplate(@RequestBody DomainTemInfo domainTemInfo ){
        try {
            Domain domain = domainBusiness.createDomainFromTemplate(domainTemInfo);
            return new ResponseEntity<>(domain,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("新增失败："+e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
