package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.dto.DeviceWithPosition;
import demo.lowcode.platform.entity.Device;
import demo.lowcode.platform.entity.DeviceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    List<Device> selectBySceneId(@Param("sceneId") Long sceneId);

    List<DeviceWithPosition> selectBySceneIdWithIntelligent(@Param("sceneId") Long sceneId);

    Device selectByCodeAndScene(@Param("code") String code, @Param("sceneId") Long sceneId);
}
