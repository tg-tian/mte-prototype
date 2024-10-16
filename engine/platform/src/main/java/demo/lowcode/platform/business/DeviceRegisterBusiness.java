package demo.lowcode.platform.business;

import demo.lowcode.platform.entity.DeviceRegister;
import demo.lowcode.platform.mapper.DeviceRegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceRegisterBusiness {
    private final DeviceRegisterMapper deviceRegisterMapper;

    @Autowired
    public DeviceRegisterBusiness(DeviceRegisterMapper deviceRegisterMapper) {this.deviceRegisterMapper = deviceRegisterMapper;}

    public void uploadRegisterData(DeviceRegister deviceRegister){
        int count = deviceRegisterMapper.countRegister(deviceRegister.getDeviceId(),deviceRegister.getSceneId());
        if(count == 0){
            deviceRegisterMapper.insert(deviceRegister);
        }else{
            throw new RuntimeException("记录已存在，不能插入重复的记录");
        }
    }
    
}
