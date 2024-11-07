package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.DeviceBindingBusiness;
import demo.lowcode.platform.business.DeviceTypeBusiness;
import demo.lowcode.platform.business.DomainBusiness;
import demo.lowcode.platform.entity.DeviceBinding;
import demo.lowcode.platform.entity.DeviceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "领域绑定接口",tags={"领域绑定管理"})
public class BindingController {

    @Resource
    DeviceBindingBusiness deviceBindingBusiness;
    @Resource
    DeviceTypeBusiness deviceTypeBusiness;
    @Resource
    DomainBusiness domainBusiness;

    /**
     * 上传被领域绑定的数据到绑定数据库
     * @param requestBody
     * @return
     */
    @PostMapping(value = "/upload-domain-component-binding")
    @ApiOperation(value = "上传绑定信息",notes = "将组件绑定信息上传至数据库")
    public ResponseEntity<?> uploadDomainComponentBinding(@RequestBody Map<String, Object> requestBody, String componentType, String domainCode) throws IOException {
        List<String> selectedCodes = (List<String>) requestBody.get("selectedCodes");
        //遍历每个被选择的设备类型
        if(Objects.equals(componentType, "Device"))
        {
            for(String selectCode : selectedCodes){
                //查询devicetype表，找到对应的deviceTypeId
                long deviceTypeId = deviceTypeBusiness.loadDeiceTypeId(selectCode);
                //查询domain表，找到对应的domainId
                long domainId = domainBusiness.getDomainIdByCode(domainCode);
                //将deviceTypeId和domainName 写入binding表
                DeviceBinding deviceBinding = new DeviceBinding(deviceTypeId,domainId);
                try{
                    deviceBindingBusiness.uploadBindingData(deviceBinding);
                }catch (RuntimeException e){
                    System.err.println("组件绑定失败: " + e.getMessage());
                    return new ResponseEntity<>("请勿重复绑定组件", HttpStatus.CONFLICT);
                }
            }
            return  new ResponseEntity<>(selectedCodes, HttpStatus.OK);
        }
        return  new ResponseEntity<>("组件绑定失败", HttpStatus.BAD_REQUEST);
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
            //查询deviceBinding获取domainName绑定的deviceTypeId列表
            long domainId = domainBusiness.getDomainIdByCode(domainCode);
            List<Long> deviceTypeIdList = deviceBindingBusiness.loadDeviceBindingId(domainId);
            //根据deviceTypeId列表，查找deviceTypeId对应的数据
            if(!deviceTypeIdList.isEmpty()){
                List<DeviceType> deviceTypeList= deviceTypeBusiness.loadDeviceData(deviceTypeIdList);
                return new ResponseEntity<>(deviceTypeList,HttpStatus.OK);
            }
            else return new ResponseEntity<>("",HttpStatus.OK);
        }
        return  new ResponseEntity<>("绑定组件查询失败", HttpStatus.BAD_REQUEST);
    }
}
