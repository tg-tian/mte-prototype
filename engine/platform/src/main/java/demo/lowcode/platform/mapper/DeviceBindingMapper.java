package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceBinding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceBindingMapper extends BaseMapper<DeviceBinding> {
    int countBinding(@Param("deviceTypeId") long deviceTypeId , @Param("domainId") long domainId);

    List<Long> loadDeviceBindingId(@Param("domainId") long domainId);
}
