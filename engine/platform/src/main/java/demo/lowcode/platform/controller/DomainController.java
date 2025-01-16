package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceTypeBusiness;
import demo.lowcode.platform.business.DomainBusiness;
import demo.lowcode.platform.dto.DomainJson;
import demo.lowcode.platform.dto.Domain_ComponentJson;
import demo.lowcode.platform.entity.DeviceType;
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
     * 读取相应组件的组件数据库
     * @param componentType
     * @return
     */
    @GetMapping(value = "/load-domain-component-data")
    public ResponseEntity<?> loadDomainComponentData(String componentType) {
        if(Objects.equals(componentType, "Device"))  //todo:暂定这样写，其他组件的类还没定义,之后其他类定义后将这里改写
        {
            List<DeviceType> deviceTypeList = deviceTypeBusiness.getDeviceTypeData();
            return  new ResponseEntity<>(deviceTypeList,HttpStatus.OK);
        }
        else return new ResponseEntity<>("文件读取错误", HttpStatus.BAD_REQUEST);
    }

}
