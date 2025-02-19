package demo.lowcode.platform.business;

import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.mapper.DeviceTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传服务类，执行数据上传数据库业务
 */
@Service
public class DeviceTypeBusiness {
    //服务层，不需要Swagger的注解描述。在Controller中使用Swagger注解。
    //使用构造函数注入 DeviceTypeMapper 确保依赖不可变

    private final DeviceTypeMapper deviceTypeMapper;

    // 使用构造函数注入，推荐的方式
    @Autowired
    public DeviceTypeBusiness(DeviceTypeMapper deviceTypeMapper) {
        this.deviceTypeMapper = deviceTypeMapper;
    }


}
