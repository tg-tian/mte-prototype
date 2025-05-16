package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Component;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ComponentMapper extends BaseMapper<Component> {
}
