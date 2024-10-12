package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Binding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BindingMapper extends BaseMapper<Binding> {
    int countByDeviceTypeId(@Param("deviceTypeId") long deviceTypeId);

}
