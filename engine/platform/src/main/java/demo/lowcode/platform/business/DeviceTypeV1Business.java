package demo.lowcode.platform.business;

import demo.lowcode.platform.entity.DeviceTypeV1;
import demo.lowcode.platform.mapper.DeviceTypeV1Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DeviceTypeV1Business {

  @Autowired
  private DeviceTypeV1Mapper deviceTypeV1Mapper;

  public List<DeviceTypeV1> list() {
    return deviceTypeV1Mapper.selectList(null);
  }

  public DeviceTypeV1 getById(Long id) {
    return deviceTypeV1Mapper.selectById(id);
  }

  public DeviceTypeV1 getByModelName(String modelName) {
    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DeviceTypeV1> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
    queryWrapper.eq("modelName", modelName);
    return deviceTypeV1Mapper.selectOne(queryWrapper);
  }

  public DeviceTypeV1 save(DeviceTypeV1 deviceTypeV1) {
    if (deviceTypeV1.getCreateTime() == null) {
      deviceTypeV1.setCreateTime(new Date());
    }
    deviceTypeV1Mapper.insert(deviceTypeV1);
    return deviceTypeV1;
  }

  public DeviceTypeV1 update(DeviceTypeV1 deviceTypeV1) {
    deviceTypeV1.setUpdateTime(new Date());
    deviceTypeV1Mapper.updateById(deviceTypeV1);
    return deviceTypeV1;
  }

  public boolean delete(Long id) {
    return deviceTypeV1Mapper.deleteById(id) > 0;
  }
}
