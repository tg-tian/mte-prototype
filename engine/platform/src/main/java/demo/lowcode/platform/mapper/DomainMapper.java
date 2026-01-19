package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Domain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DomainMapper extends BaseMapper<Domain> {
    long getDomainIdByName(@Param("domainName") String domainName);
    Domain getDomainByCode(@Param("domainCode") String domainCode);
}
