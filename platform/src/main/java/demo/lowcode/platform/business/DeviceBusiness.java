package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.platform.dto.DeviceWithPosition;
import demo.lowcode.platform.dto.NewDevice;
import demo.lowcode.platform.entity.Device;
import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.entity.Scene;
import demo.lowcode.platform.mapper.DeviceConnectionMapper;
import demo.lowcode.platform.mapper.DeviceMapper;
import demo.lowcode.platform.mapper.DeviceTypeMapper;
import demo.lowcode.platform.mapper.SceneMapper;
import demo.lowcode.platform.model.device.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeviceBusiness extends ServiceImpl<DeviceMapper, Device> implements IService<Device> {
    private final DeviceMapper deviceMapper;
    private final DeviceTypeMapper deviceTypeMapper;
    private final SceneMapper sceneMapper;
    private final DeviceConnectionMapper deviceConnectionMapper;

    @Autowired
    public DeviceBusiness(DeviceMapper deviceMapper, DeviceTypeMapper deviceTypeMapper, SceneMapper sceneMapper, DeviceConnectionMapper deviceConnectionMapper) {
        this.deviceMapper = deviceMapper;
        this.deviceTypeMapper = deviceTypeMapper;
        this.sceneMapper = sceneMapper;
        this.deviceConnectionMapper = deviceConnectionMapper;
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
        device.setIntelligent(true);
        if (newDevice.getDeviceTypeId() != null) {
            DeviceType deviceType = deviceTypeMapper.selectById(newDevice.getDeviceTypeId());
            if (deviceType == null) {
                throw new RuntimeException("该设备类型不存在");
            }
            device.setDeviceType(deviceType);
            device.setDeviceTypeId(newDevice.getDeviceTypeId());
            List<Property> properties = null;
            if (deviceType.getModel() != null) {
                properties = deviceType.getModel().getProperties();
            }
            if (properties != null) {
                boolean hasObjectProperty = properties.stream()
                        .anyMatch(p -> p.getIdentify() != null && p.getIdentify().startsWith("OBJECT"));
                if (hasObjectProperty) {
                    device.setIntelligent(false);
                }
            }
        } else {
            throw new RuntimeException("请选择设备类型");
        }

        if (newDevice.getSceneId() != null) {
            Scene scene = sceneMapper.selectById(newDevice.getSceneId());
            if (scene == null) {
                throw new RuntimeException("该领域不存在");
            }
            device.setScene(scene);
            device.setSceneId(scene.getSceneId());
        } else {
            throw new RuntimeException("请选择领域");
        }
        if (deviceMapper.selectByCodeAndScene(newDevice.getCode(), newDevice.getSceneId()) != null) {
            throw new RuntimeException("对应设备编码已存在");
        }

        device.setDeviceCode(newDevice.getCode());
        device.setDeviceName(newDevice.getName());
        device.setProtocolType(newDevice.getProtocolType());
        device.setProtocolConfig(newDevice.getProtocolConfig());
        device.setCreateTime(new Date());
        device.setStatus(2);
        device.setDeviceLocation(newDevice.getDeviceLocation());
        device.setDevicePosition(newDevice.getDevicePosition());
        deviceMapper.insert(device);

        return device;
    }

    public List<DeviceType> getDeviceTypeListByScene(Long sceneId) {
        return deviceTypeMapper.selectBySceneId(sceneId);
    }

    public Device updateDevice(Long id, NewDevice newDevice) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new RuntimeException("设备不存在");
        }

        if (newDevice.getDeviceTypeId() != null) {
            DeviceType deviceType = deviceTypeMapper.selectById(newDevice.getDeviceTypeId());
            if (deviceType == null) {
                throw new RuntimeException("该设备类型不存在");
            }
            device.setDeviceType(deviceType);
            device.setDeviceTypeId(newDevice.getDeviceTypeId());
        } else {
            throw new RuntimeException("请选择设备类型");
        }

        device.setDeviceName(newDevice.getName());
        device.setDeviceCode(newDevice.getCode());
        device.setProtocolType(newDevice.getProtocolType());
        device.setProtocolConfig(newDevice.getProtocolConfig());
        device.setDeviceLocation(newDevice.getDeviceLocation());
        device.setDevicePosition(newDevice.getDevicePosition());

        deviceMapper.updateById(device);

        return device;
    }

    public void deleteDeviceByID(Long id) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new RuntimeException("该设备不存在");
        }

        deviceMapper.deleteById(device.getId());
    }

    public List<NewDevice> getDeviceConnectionsListByScene(Long sceneId) {
        List<NewDevice> newDevices = new ArrayList<>();
        List<DeviceWithPosition> devices = deviceMapper.selectBySceneIdWithIntelligent(sceneId);
        for (DeviceWithPosition device : devices) {
            device.setDeviceType(deviceTypeMapper.selectById(device.getDeviceTypeId()));
            NewDevice newDevice = new NewDevice();
            newDevice.setId(device.getId());
            newDevice.setName(device.getDeviceName());
            newDevice.setDeviceType(device.getDeviceType());
            newDevice.setConnections(new ArrayList<>());
            newDevice.setIntelligent(device.isIntelligent());
            deviceConnectionMapper.selectConnectionsByDeviceId(device.getId()).forEach(
                    inDevice -> {
                        NewDevice.Connection conn = new NewDevice.Connection();
                        conn.setId(inDevice.getId());
                        conn.setDeviceName(inDevice.getDeviceName());
                        conn.setPosition(inDevice.getPosition());
                        newDevice.getConnections().add(conn);
                    }
            );
            newDevices.add(newDevice);
        }
        return newDevices;
    }

    public void deleteConnection(Long sourceId, Long targetId) {

        deviceConnectionMapper.delteConnection(sourceId, targetId);

    }

    public void addConnection(Long sourceId, Long targetId,String position) {
        Device sDevice = deviceMapper.selectById(sourceId);
        Device tDevice = deviceMapper.selectById(targetId);
        if (!deviceConnectionMapper.addConnection(sourceId, sDevice.getDeviceName(),
                targetId,tDevice.getDeviceName(), position)) {
            throw new RuntimeException("添加连接失败");
        }
    }
}
