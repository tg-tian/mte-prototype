package demo.lowcode.platform.business;

import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.mapper.DeviceTypeMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 上传服务类，执行数据上传数据库业务
 */
@Service
public class UploadBusiness {
    //服务层，不需要Swagger的注解描述。在Controller中使用Swagger注解。
    //使用构造函数注入 DeviceTypeMapper 确保依赖不可变

    private final DeviceTypeMapper deviceTypeMapper;

    // 使用构造函数注入，推荐的方式
    @Autowired
    public UploadBusiness(DeviceTypeMapper deviceTypeMapper) {
        this.deviceTypeMapper = deviceTypeMapper;
    }

    /**
     * 新增设备类型，上传数据库
     * @param deviceType
     */
    public void deviceTypeUpload(DeviceType deviceType){
        deviceTypeMapper.insert(deviceType);
    }
}
