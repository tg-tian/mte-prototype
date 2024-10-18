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

    public void deviceUpload(Device device) {
        int count = deviceMapper.countByDeviceCode(device.getDeviceCode());
        if(count == 0){
            deviceMapper.insertDevice(device.getDeviceCode(),device.getDeviceName(),device.getDeviceTypeId(),device.getVersionNumber(),device.getManufacturer());
        }else{
            throw new RuntimeException("deviceCode 已存在，不能插入重复的记录");
        }
    }
    /**
     * 获取deviceId
     * @return
     */
    public Long loadDeviceId(String deviceCode){
        return deviceMapper.getDeviceId(deviceCode);
    }

}
