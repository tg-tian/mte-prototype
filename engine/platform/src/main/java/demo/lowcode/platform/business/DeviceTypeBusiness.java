package demo.lowcode.platform.business;

import demo.lowcode.platform.mapper.DeviceTypeOldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 上传服务类，执行数据上传数据库业务
 */
@Service
public class DeviceTypeBusiness {

    private final DeviceTypeOldMapper deviceTypeOldMapper;

    // 使用构造函数注入，推荐的方式
    @Autowired
    public DeviceTypeBusiness(DeviceTypeOldMapper deviceTypeOldMapper) {
        this.deviceTypeOldMapper = deviceTypeOldMapper;
    }


}
