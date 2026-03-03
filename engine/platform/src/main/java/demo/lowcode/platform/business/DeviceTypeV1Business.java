package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.lowcode.platform.entity.DeviceTypeV1;
import demo.lowcode.platform.mapper.DeviceTypeV1Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class DeviceTypeV1Business {

  private static final Logger logger = Logger.getLogger(DeviceTypeV1Business.class.getName());

  @Autowired
  private DeviceTypeV1Mapper deviceTypeV1Mapper;

  public List<DeviceTypeV1> list() {
    return deviceTypeV1Mapper.selectList(null);
  }

  public IPage<DeviceTypeV1> page(Page<DeviceTypeV1> page, QueryWrapper<DeviceTypeV1> queryWrapper) {
    return deviceTypeV1Mapper.selectPage(page, queryWrapper);
  }

  public DeviceTypeV1 getById(Long id) {
    return deviceTypeV1Mapper.selectById(id);
  }

  public DeviceTypeV1 getByModelId(String modelId) {
    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DeviceTypeV1> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
    queryWrapper.eq("model_id", modelId);
    return deviceTypeV1Mapper.selectOne(queryWrapper);
  }

  public DeviceTypeV1 save(DeviceTypeV1 deviceTypeV1) {
    // 创建时确保ID为null，让数据库自动生成
    deviceTypeV1.setId(null);

    // 重置AUTO_INCREMENT，确保ID从当前最大ID+1开始（避免删除记录后ID跳跃）
    Long maxId = deviceTypeV1Mapper.selectMaxId();
    if (maxId == null) {
      maxId = 0L;
    }
    Long nextId = maxId + 1;

    // 直接重置AUTO_INCREMENT为最大ID+1，确保新ID连续
    try {
      logger.info("重置AUTO_INCREMENT: 当前最大ID=" + maxId + ", 重置为=" + nextId);
      deviceTypeV1Mapper.resetAutoIncrement(nextId);
      logger.info("AUTO_INCREMENT重置成功");
    } catch (Exception e) {
      // 如果重置失败，记录日志但不影响插入操作
      logger.warning("重置AUTO_INCREMENT失败: " + e.getMessage());
      e.printStackTrace();
    }

    deviceTypeV1Mapper.insert(deviceTypeV1);
    return deviceTypeV1;
  }

  public DeviceTypeV1 update(DeviceTypeV1 deviceTypeV1) {
    deviceTypeV1Mapper.updateById(deviceTypeV1);
    return deviceTypeV1;
  }

  public boolean delete(Long id) {
    boolean deleted = deviceTypeV1Mapper.deleteById(id) > 0;

    // 删除后重置AUTO_INCREMENT，确保新ID从当前最大ID+1开始
    if (deleted) {
      Long maxId = deviceTypeV1Mapper.selectMaxId();
      if (maxId == null) {
        maxId = 0L;
      }
      Long nextId = maxId + 1;
      deviceTypeV1Mapper.resetAutoIncrement(nextId);
    }

    return deleted;
  }
}
