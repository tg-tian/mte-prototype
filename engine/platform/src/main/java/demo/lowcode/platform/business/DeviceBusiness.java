package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.lowcode.platform.entity.DeviceOld;
import demo.lowcode.platform.mapper.DeviceOldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceBusiness extends ServiceImpl<DeviceOldMapper, DeviceOld> implements IService<DeviceOld> {
    private final DeviceOldMapper deviceOldMapper;

    @Autowired
    public DeviceBusiness( DeviceOldMapper deviceOldMapper) {this.deviceOldMapper = deviceOldMapper;}

}
