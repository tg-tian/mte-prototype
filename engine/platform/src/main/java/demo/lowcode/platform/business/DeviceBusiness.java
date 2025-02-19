package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.lowcode.platform.entity.Device;
import demo.lowcode.platform.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceBusiness extends ServiceImpl<DeviceMapper, Device> implements IService<Device> {
    private final DeviceMapper deviceMapper;

    @Autowired
    public DeviceBusiness( DeviceMapper deviceMapper) {this.deviceMapper = deviceMapper;}

}
