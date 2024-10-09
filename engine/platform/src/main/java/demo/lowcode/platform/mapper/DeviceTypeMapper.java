package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备类型数据接入接口，实现设备类型数据库操作
 */
@Mapper
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {
     void insertDeviceType(@Param("deviceTypeCode") String deviceTypeCode, @Param("deviceTypeName") String deviceTypeName, @Param("imgPath") String imgPath);

}
