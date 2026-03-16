package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.lowcode.platform.entity.DeviceModel;
import demo.lowcode.platform.mapper.DeviceModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class DeviceModelBusiness {

  private static final Logger logger = Logger.getLogger(DeviceModelBusiness.class.getName());

  @Autowired
  private DeviceModelMapper deviceModelMapper;

  public List<DeviceModel> list() {
    return deviceModelMapper.selectList(null);
  }

  public IPage<DeviceModel> page(Page<DeviceModel> page, QueryWrapper<DeviceModel> queryWrapper) {
    return deviceModelMapper.selectPage(page, queryWrapper);
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
}


