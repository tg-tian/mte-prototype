package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.dto.DeviceWithPosition;
import demo.lowcode.platform.entity.Device;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DeviceConnectionMapper extends BaseMapper<Device> {

    List<DeviceWithPosition> selectConnectionsByDeviceId(Long deviceId);

    void delteConnection(Long sourceId, Long targetId);

    boolean addConnection(Long sourceId, String sName, Long targetId, String tName, String position);

}
