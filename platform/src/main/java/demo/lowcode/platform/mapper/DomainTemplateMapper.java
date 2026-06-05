package demo.lowcode.platform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DomainTemplateMapper {
    void insertDomainTemplateRelation(@Param("domainCode") String domainCode, @Param("templateId") Long templateId);

    void batchInsertDomainTemplateRelations(@Param("domainCode") String domainCode, @Param("templateIds") List<Long> templateIds);

    void deleteDomainTemplateRelation(@Param("domainCode") String domainCode, @Param("templateId") Long templateId);

    void deleteDomainTemplateRelationsByDomainCode(@Param("domainCode") String domainCode);
}

