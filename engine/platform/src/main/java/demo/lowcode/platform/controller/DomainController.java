package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceTypeBusiness;
import demo.lowcode.platform.business.DomainBusiness;
import demo.lowcode.platform.dto.DomainJson;
import demo.lowcode.platform.dto.Domain_ComponentJson;
import demo.lowcode.platform.dto.newDomain;
import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.entity.Domain;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
@Api(value = "领域接口",tags={"领域内容管理"})
public class DomainController {
    @Resource
    DomainBusiness domainBusiness;
    @Resource
    DeviceTypeBusiness deviceTypeBusiness ;

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
    public ResponseEntity<?> getDomainListbyId(@PathVariable Long id){
        Domain response = domainBusiness.getDomainByID(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(value = "/domains")
    public ResponseEntity<?> createDomain(@RequestBody newDomain newDomain){
        try {
            domainBusiness.createDomain(newDomain);
            return new ResponseEntity<>("创建成功",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("创建失败",HttpStatus.CONFLICT);
        }
    }

    @PutMapping (value = "/domains/{id}")
    public ResponseEntity<?> changeDomain(@PathVariable Long id ,@RequestBody newDomain newDomain){
        try {
            domainBusiness.changeDomainByID(id,newDomain);
            return new ResponseEntity<>("修改成功",HttpStatus.OK);
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
}
