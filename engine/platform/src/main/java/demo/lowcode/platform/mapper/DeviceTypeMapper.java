package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备类型数据接入接口，实现设备类型数据库操作
 */
@Mapper
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {
     void insertDeviceType(@Param("deviceTypeCode") String deviceTypeCode, @Param("deviceTypeName") String deviceTypeName, @Param("imgPath") String imgPath);
     int countByDeviceTypeCode(@Param("deviceTypeCode") String deviceTypeCode);
     long loadDeviceId(@Param("deviceTypeCode") String deviceTypeCode);
     List<DeviceType> selectBatchIds(@Param("deviceTypeIdList") List<Long> deviceTypeIdList);
     String getDeviceTypeCode(@Param("deviceTypeId") Long deviceTypeId);
     DeviceType selectByCode(@Param("deviceTypeCode") String deviceTypeCode);
}
