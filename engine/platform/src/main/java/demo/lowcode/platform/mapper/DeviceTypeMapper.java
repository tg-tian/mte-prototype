package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备类型数据接入接口，实现设备类型数据库操作
 */
@Mapper
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {
/**
    @Insert("INSERT INTO devicetype (deviceTypeCode,deviceTypeName,imgPath) VALUES(#{deviceTypeCode},#{deviceTypeName},#{imgPath})")
    @Options(useGeneratedKeys = true, keyProperty = "deviceTypeNumber")  // 是为了在插入记录时自动获取数据库生成的主键值，并将其设置到 DeviceType 实体的 deviceTypeNumber 字段中。
    void insertDeviceType(DeviceType deviceType);
    */
}
