package demo.lowcode.platform.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
@Api(value = "领域绑定接口",tags={"领域绑定管理"})
public class DomainDeviceTypeBindingController {

    /**
     * TODO:上传被领域绑定的数据到绑定数据库
     * @param requestBody
     * @return
     */
    @PostMapping(value = "/upload-domain-component-binding")
    @ApiOperation(value = "上传绑定信息",notes = "将组件绑定信息上传至数据库")
    public ResponseEntity<?> uploadDomainComponentBinding(@RequestBody Map<String, Object> requestBody, String componentType, String domainCode) throws IOException {
        List<String> selectedCodes = (List<String>) requestBody.get("selectedCodes");
        //遍历每个被选择的设备类型
        return  new ResponseEntity<>(selectedCodes, HttpStatus.OK);
    }


    /**
     * 获取领域被绑定的组件信息
     * @param componentType
     * @param domainCode
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/load-domain-component-binding")
    @ApiOperation(value = "获取绑定信息",notes = "获取领域被绑定的组件信息")
    public ResponseEntity<?> loadDomainComponentBinding(String componentType ,String domainCode) throws IOException{
        //根据componentType查询对应的binding表
        if(Objects.equals(componentType, "Device")){
            return new ResponseEntity<>("",HttpStatus.OK);
        }
        return  new ResponseEntity<>("绑定组件查询失败", HttpStatus.BAD_REQUEST);
    }
}
