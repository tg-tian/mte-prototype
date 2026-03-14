package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceTypeOld;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备类型数据接入接口，实现设备类型数据库操作
 */
@Mapper
public interface DeviceTypeOldMapper extends BaseMapper<DeviceTypeOld> {
     void insertDeviceType(@Param("deviceTypeCode") String deviceTypeCode, @Param("deviceTypeName") String deviceTypeName, @Param("imgPath") String imgPath);
     int countByDeviceTypeCode(@Param("deviceTypeCode") String deviceTypeCode);
     long loadDeviceId(@Param("deviceTypeCode") String deviceTypeCode);
     List<DeviceTypeOld> selectBatchIds(@Param("deviceTypeIdList") List<Long> deviceTypeIdList);
     String getDeviceTypeCode(@Param("deviceTypeId") Long deviceTypeId);
     DeviceTypeOld selectByCode(@Param("deviceTypeCode") String deviceTypeCode);
     void updatePublish(@Param("deviceTypeCode")String deviceTypeCode);
}
