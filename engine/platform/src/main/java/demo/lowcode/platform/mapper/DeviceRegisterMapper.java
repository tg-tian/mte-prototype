package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceRegister;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceRegisterMapper extends BaseMapper<DeviceRegister> {
    int countRegister(@Param("deviceId") long deviceId , @Param("sceneId") long sceneId);

}
