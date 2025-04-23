package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.entity.DomainComponent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DomainComponentMapper extends BaseMapper<DomainComponent> {
    DomainComponent selectByDomainAndDeviceType(@Param("domainId") Long domainId, @Param("deviceTypeId") Long deviceTypeId);
    List<DomainComponent> selectByDeviceType(@Param("deviceTypeId") Long deviceTypeId);
}
