package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {
    List<DeviceType> selectByDomainId(@Param("domainId") Long domainId);
    DeviceType selectByCode(@Param("code") String code);
}
