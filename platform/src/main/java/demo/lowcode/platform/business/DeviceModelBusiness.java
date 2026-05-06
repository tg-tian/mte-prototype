package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.lowcode.platform.entity.DeviceModel;
import demo.lowcode.platform.entity.Domain;
import demo.lowcode.platform.entity.DomainDeviceModel;
import demo.lowcode.platform.mapper.DeviceModelMapper;
import demo.lowcode.platform.mapper.DomainDeviceModelMapper;
import demo.lowcode.platform.mapper.DomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class DeviceModelBusiness {

  private static final Logger logger = Logger.getLogger(DeviceModelBusiness.class.getName());

  @Autowired
  private DeviceModelMapper deviceModelMapper;

  @Autowired
  private DomainMapper domainMapper;

  @Autowired
  private DomainDeviceModelMapper domainDeviceModelMapper;

  public List<DeviceModel> list() {
    return deviceModelMapper.selectList(null);
  }

  public List<DeviceModel> list(Long domainId) {
    if (domainId == null) {
      return list();
    }
    return deviceModelMapper.selectByDomainId(domainId);
  }

  public IPage<DeviceModel> page(Page<DeviceModel> page, QueryWrapper<DeviceModel> queryWrapper) {
    return deviceModelMapper.selectPage(page, queryWrapper);
  }

  public List<DeviceModel> pageByDomainId(Integer current, Integer size, Long domainId) {
    List<DeviceModel> all = deviceModelMapper.selectByDomainId(domainId);
    int fromIndex = Math.max((current - 1) * size, 0);
    if (fromIndex >= all.size()) {
      return Collections.emptyList();
    }
    int toIndex = Math.min(fromIndex + size, all.size());
    return all.subList(fromIndex, toIndex);
  }

  public DeviceModel getById(Long id) {
    return deviceModelMapper.selectById(id);
  }

  public DeviceModel getByModelId(String modelId) {
    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DeviceModel> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
    queryWrapper.eq("modelId", modelId);
    return deviceModelMapper.selectOne(queryWrapper);
  }

  public DeviceModel save(DeviceModel deviceModel) {
    // 创建时确保ID为null，让数据库自动生成
    deviceModel.setId(null);

    // 重置AUTO_INCREMENT，确保ID从当前最大ID+1开始（避免删除记录后ID跳跃）
    Long maxId = deviceModelMapper.selectMaxId();
    if (maxId == null) {
      maxId = 0L;
    }
    Long nextId = maxId + 1;

    // 直接重置AUTO_INCREMENT为最大ID+1，确保新ID连续
    try {
      logger.info("重置AUTO_INCREMENT: 当前最大ID=" + maxId + ", 重置为=" + nextId);
      deviceModelMapper.resetAutoIncrement(nextId);
      logger.info("AUTO_INCREMENT重置成功");
    } catch (Exception e) {
      // 如果重置失败，记录日志但不影响插入操作
      logger.warning("重置AUTO_INCREMENT失败: " + e.getMessage());
      e.printStackTrace();
    }

    deviceModelMapper.insert(deviceModel);
    return deviceModel;
  }

  public DeviceModel update(DeviceModel deviceModel) {
    deviceModelMapper.updateById(deviceModel);
    return deviceModel;
  }

  public boolean delete(Long id) {
    boolean deleted = deviceModelMapper.deleteById(id) > 0;

    // 删除后重置AUTO_INCREMENT，确保新ID从当前最大ID+1开始
    if (deleted) {
      Long maxId = deviceModelMapper.selectMaxId();
      if (maxId == null) {
        maxId = 0L;
      }
      Long nextId = maxId + 1;
      deviceModelMapper.resetAutoIncrement(nextId);
    }

    return deleted;
  }

  @Transactional
  public void bindDomain(Long deviceModelId, Long domainId) {
    Domain domain = domainMapper.selectById(domainId);
    if (domain == null) {
      throw new RuntimeException("领域不存在");
    }
    DeviceModel deviceModel = deviceModelMapper.selectById(deviceModelId);
    if (deviceModel == null) {
      throw new RuntimeException("设备模型不存在");
    }
    DomainDeviceModel existed = domainDeviceModelMapper.selectByDomainAndDeviceModel(domainId, deviceModelId);
    if (existed != null) {
      throw new RuntimeException("绑定失败：该设备类型已绑定");
    }
    domainDeviceModelMapper.insertDomainDeviceModelRelation(domainId, deviceModelId);
  }

  @Transactional
  public void unbindDomain(Long deviceModelId, Long domainId) {
    Domain domain = domainMapper.selectById(domainId);
    if (domain == null) {
      throw new RuntimeException("领域不存在");
    }
    DomainDeviceModel existed = domainDeviceModelMapper.selectByDomainAndDeviceModel(domainId, deviceModelId);
    if (existed == null) {
      throw new RuntimeException("两者未绑定");
    }
    domainDeviceModelMapper.deleteDomainDeviceModelRelation(domainId, deviceModelId);
  }
}


