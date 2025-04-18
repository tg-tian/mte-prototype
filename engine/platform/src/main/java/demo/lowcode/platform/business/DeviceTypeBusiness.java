package demo.lowcode.platform.business;

import demo.lowcode.platform.dto.NewDeviceType;
import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.entity.DomainComponent;
import demo.lowcode.platform.mapper.DeviceTypeMapper;
import demo.lowcode.platform.mapper.DomainComponentMapper;
import demo.lowcode.platform.model.device.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 上传服务类，执行数据上传数据库业务
 */
@Service
public class DeviceTypeBusiness {
    private final DeviceTypeMapper deviceTypeMapper;
    private final DomainComponentMapper domainComponentMapper;

    // 使用构造函数注入，推荐的方式
    @Autowired
    public DeviceTypeBusiness(DeviceTypeMapper deviceTypeMapper, DomainComponentMapper domainComponentMapper) {
        this.deviceTypeMapper = deviceTypeMapper;
        this.domainComponentMapper = domainComponentMapper;
    }

    public List<DeviceType> getDeviceTypeListByDomain(Long domainId) {
        return deviceTypeMapper.selectByDomainId(domainId);
    }

    public List<DeviceType> getDeviceTypeList() {
        return deviceTypeMapper.selectList(null);
    }

    public void bindDomainAndDeviceType(Long domainId, Long deviceTypeId) {
        if (domainComponentMapper.selectByDomainAndDeviceType(domainId, deviceTypeId) != null){
            throw new RuntimeException("绑定失败：该组件已绑定");
        }
        DomainComponent component = new DomainComponent();
        component.setDomainId(domainId);
        component.setComponentId(deviceTypeId);
        component.setComponentType("devicetype");
        domainComponentMapper.insert(component);
    }

    public DeviceType getDeviceTypeById(Long id) {
        return deviceTypeMapper.selectById(id);
    }

    public DeviceType createDeviceType(String code, String name, String description) {
        if(deviceTypeMapper.selectByCode(code) != null){
            throw new RuntimeException("对应设备类型编码已存在");
        }

        DeviceType deviceType = new DeviceType();
        deviceType.setId(null);
        deviceType.setCode(code);
        deviceType.setName(name);
        deviceType.setDescription(description);
        deviceType.setCreateTime(new Date());
        deviceTypeMapper.insert(deviceType);

        return deviceType;
    }

    public DeviceType updateDeviceType(Long id, NewDeviceType newDeviceType) {
        DeviceType deviceType = deviceTypeMapper.selectById(id);
        if(deviceType == null){
            throw new RuntimeException("该设备类型不存在");
        }

        deviceType.setCode(newDeviceType.getCode());
        deviceType.setName(newDeviceType.getName());
        deviceType.setDescription(newDeviceType.getDescription());
        deviceType.setUpdateTime(new Date());
        deviceTypeMapper.updateById(deviceType);

        return deviceType;
    }

    public void deleteDeviceTypeByID(Long id) {
        DeviceType deviceType = deviceTypeMapper.selectById(id);
        if(deviceType == null){
            throw new RuntimeException("该设备类型不存在");
        }
        List<DomainComponent> components = domainComponentMapper.selectByDeviceType(id);
        if(components!=null && components.size()!=0){
            throw new RuntimeException("存在领域绑定该设备类型，无法删除");
        }

        deviceTypeMapper.deleteById(deviceType.getId());
    }

    public void unbindDomainAndDeviceType(Long domainId, Long deviceTypeId) {
        DomainComponent domainComponent = domainComponentMapper.selectByDomainAndDeviceType(domainId, deviceTypeId);
        if (domainComponent == null){
            throw new RuntimeException("两者未绑定");
        }
        domainComponentMapper.deleteById(domainComponent.getId());
    }

    public DeviceType updateModel(Long deviceTypeId, Model model) {
        DeviceType deviceType = deviceTypeMapper.selectById(deviceTypeId);
        if(deviceType == null){
            throw new RuntimeException("该设备类型不存在");
        }
        deviceType.setModel(model);
        System.out.println(deviceType);
        deviceTypeMapper.updateById(deviceType);
        return deviceType;
    }
}
