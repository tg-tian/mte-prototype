package demo.lowcode.platform.business;

import demo.lowcode.platform.entity.DeviceBinding;
import demo.lowcode.platform.mapper.DeviceBindingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DeviceBindingBusiness {
    private final DeviceBindingMapper deviceBindingMapper;

    @Autowired
    public DeviceBindingBusiness(DeviceBindingMapper deviceBindingMapper) {this.deviceBindingMapper = deviceBindingMapper;}


    public void uploadBindingData(DeviceBinding deviceBinding) {
        int count = deviceBindingMapper.countBinding(deviceBinding.getDeviceTypeId(),deviceBinding.getDomainId());
        if(count == 0){
            deviceBindingMapper.insert(deviceBinding);
        }else{
            throw new RuntimeException("记录已存在，不能插入重复的记录");
        }
    }

    /**
     * 根据domainId查找对应的DeviceBindingId
     * @return
     */
    public List<Long> loadDeviceBindingId(long domainId) {
        return deviceBindingMapper.loadDeviceBindingId(domainId);
    }
}
