package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceTypeV1;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DeviceTypeV1Mapper extends BaseMapper<DeviceTypeV1> {
  /**
   * 查询当前表中的最大ID
   */
  @Select("SELECT COALESCE(MAX(id), 0) FROM devicetype_new")
  Long selectMaxId();

  /**
   * 查询当前AUTO_INCREMENT值
   */
  @Select("SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'lowcodeDemo' AND TABLE_NAME = 'devicetype_new'")
  Long selectAutoIncrement();

  /**
   * 重置AUTO_INCREMENT为指定值
   */
  @Update("ALTER TABLE devicetype_new AUTO_INCREMENT = #{nextId}")
  void resetAutoIncrement(Long nextId);
}
