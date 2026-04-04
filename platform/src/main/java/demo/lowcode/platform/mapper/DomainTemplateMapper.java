package demo.lowcode.platform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DomainTemplateMapper {
    void insertDomainTemplateRelation(@Param("domainId") Long domainId, @Param("templateId") Long templateId);

    void batchInsertDomainTemplateRelations(@Param("domainId") Long domainId, @Param("templateIds") List<Long> templateIds);

    void deleteDomainTemplateRelation(@Param("domainId") Long domainId, @Param("templateId") Long templateId);

    void deleteDomainTemplateRelationsByDomainId(@Param("domainId") Long domainId);
}

