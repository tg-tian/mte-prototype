package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DeviceModelMapper extends BaseMapper<DeviceModel> {
  /**
   * 查询当前表中的最大ID
   */
  @Select("SELECT COALESCE(MAX(id), 0) FROM device_model")
  Long selectMaxId();

  /**
   * 查询当前AUTO_INCREMENT值
   */
  @Select("SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'device_model'")
  Long selectAutoIncrement();

  /**
   * 重置AUTO_INCREMENT为指定值
   */
  @Update("ALTER TABLE device_model AUTO_INCREMENT = #{nextId}")
  void resetAutoIncrement(Long nextId);
}


