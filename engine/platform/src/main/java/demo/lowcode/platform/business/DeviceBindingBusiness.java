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


    public void uploadBindingData(DeviceBinding deviceBinding) throws IOException{
        int count = deviceBindingMapper.countByDeviceTypeId(deviceBinding.getDeviceTypeId());
        if(count == 0){
            deviceBindingMapper.insert(deviceBinding);
        }else{
            throw new RuntimeException("deviceTypeId已存在，不能插入重复的记录");
        }
    }

    /**
     * 根据domainId查找对应的DeviceBindingId
     * @param domainId
     * @return
     * @throws IOException
     */
    public List<Long> loadDeviceBindingId(long domainId) throws IOException{
        List<Long> deviceBindingId = deviceBindingMapper.loadDeviceBindingId(domainId);
        return deviceBindingId;
    }
}
