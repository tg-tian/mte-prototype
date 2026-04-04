package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TemplateMapper extends BaseMapper<Template> {
    List<Template> getDomainTemplate(@Param("domainId") Long domainId);
    
    Template selectByTemplateId(@Param("templateId") Long templateId);
}
