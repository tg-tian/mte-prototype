package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceLibrary;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceLibraryMapper extends BaseMapper<DeviceLibrary> {
}
