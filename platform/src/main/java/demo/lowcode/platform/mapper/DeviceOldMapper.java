package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceOld;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceOldMapper extends BaseMapper<DeviceOld> {
    void insertDevice(@Param("deviceCode") String deviceCode , @Param("deviceName") String deviceName, @Param("deviceTypeId") long deviceTypeId,@Param("versionNumber") String versionNumber,@Param("manufacturer") String manufacturer);
    int countByDeviceCode(@Param("deviceCode") String deviceCode);
    long getDeviceId(@Param("deviceCode") String deviceCode);
}
