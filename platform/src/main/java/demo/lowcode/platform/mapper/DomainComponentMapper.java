package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.DomainComponent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DomainComponentMapper extends BaseMapper<DomainComponent> {
    void batchInsertDomainComponents(@Param("domainId") Long domainId, @Param("componentIds") List<Long> dcomponentIds);
    DomainComponent selectByDomainAndComponent(@Param("domainId") Long domainId, @Param("componentId") Long componentId);
}
