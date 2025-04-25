package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.lowcode.platform.dto.NewDevice;
import demo.lowcode.platform.entity.*;
import demo.lowcode.platform.mapper.DeviceMapper;
import demo.lowcode.platform.mapper.DeviceOldMapper;
import demo.lowcode.platform.mapper.DeviceTypeMapper;
import demo.lowcode.platform.mapper.SceneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DeviceBusiness extends ServiceImpl<DeviceOldMapper, DeviceOld> implements IService<DeviceOld> {
    private final DeviceMapper deviceMapper;
    private final DeviceTypeMapper deviceTypeMapper;
    private final SceneMapper sceneMapper;

    @Autowired
    public DeviceBusiness( DeviceMapper deviceMapper, DeviceTypeMapper deviceTypeMapper, SceneMapper sceneMapper) {
        this.deviceMapper = deviceMapper;
        this.deviceTypeMapper = deviceTypeMapper;
        this.sceneMapper = sceneMapper;
    }

    public List<Device> getDeviceListByScene(Long sceneId) {
        List<Device> deviceList = deviceMapper.selectBySceneId(sceneId);
        for (Device device : deviceList) {
            device.setDeviceType(deviceTypeMapper.selectById(device.getDeviceTypeId()));
            device.setScene(sceneMapper.selectById(device.getSceneId()));
        }
        return deviceList;
    }

    public Device createDevice(NewDevice newDevice) {
        Device device = new Device();
        if (newDevice.getDeviceTypeId()!=null){
            DeviceType deviceType = deviceTypeMapper.selectById(newDevice.getDeviceTypeId());
            if (deviceType==null){
                throw new RuntimeException("该设备类型不存在");
            }
            device.setDeviceType(deviceType);
            device.setDeviceTypeId(newDevice.getDeviceTypeId());
        }else{
            throw new RuntimeException("请选择设备类型");
        }

        if (newDevice.getSceneId()!=null){
            Scene scene = sceneMapper.selectById(newDevice.getSceneId());
            if (scene==null){
                throw new RuntimeException("该领域不存在");
            }
            device.setScene(scene);
            device.setSceneId(scene.getSceneId());
        }else{
            throw new RuntimeException("请选择领域");
        }
        if(deviceMapper.selectByCodeAndScene(newDevice.getCode(), newDevice.getSceneId()) != null){
            throw new RuntimeException("对应设备编码已存在");
        }

        device.setDeviceCode(newDevice.getCode());
        device.setDeviceName(newDevice.getName());
        device.setProtocolType(newDevice.getProtocolType());
        device.setProtocolConfig(newDevice.getProtocolConfig());
        device.setCreateTime(new Date());
        device.setStatus(2);
        deviceMapper.insert(device);

        return device;
    }

    public List<DeviceType> getDeviceTypeListByScene(Long sceneId) {
        return deviceTypeMapper.selectBySceneId(sceneId);
    }

    public Device updateDevice(Long id, NewDevice newDevice) {
        Device device = deviceMapper.selectById(id);
        if(device == null){
            throw new RuntimeException("设备不存在");
        }

        if (newDevice.getDeviceTypeId()!=null){
            DeviceType deviceType = deviceTypeMapper.selectById(newDevice.getDeviceTypeId());
            if (deviceType==null){
                throw new RuntimeException("该设备类型不存在");
            }
            device.setDeviceType(deviceType);
            device.setDeviceTypeId(newDevice.getDeviceTypeId());
        }else{
            throw new RuntimeException("请选择设备类型");
        }

        device.setDeviceName(newDevice.getName());
        device.setDeviceCode(newDevice.getCode());
        device.setProtocolType(newDevice.getProtocolType());
        device.setProtocolConfig(newDevice.getProtocolConfig());
        deviceMapper.updateById(device);

        return device;
    }

    public void deleteDeviceByID(Long id) {
        Device device = deviceMapper.selectById(id);
        if(device == null){
            throw new RuntimeException("该设备不存在");
        }

        deviceMapper.deleteById(device.getId());
    }
}
