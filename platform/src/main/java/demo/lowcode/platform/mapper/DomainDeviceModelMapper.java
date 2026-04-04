package demo.lowcode.platform.mapper;

import demo.lowcode.platform.entity.DomainDeviceModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DomainDeviceModelMapper {
    void insertDomainDeviceModelRelation(@Param("domainId") Long domainId, @Param("deviceModelId") Long deviceModelId);

    DomainDeviceModel selectByDomainAndDeviceModel(@Param("domainId") Long domainId, @Param("deviceModelId") Long deviceModelId);

    void deleteDomainDeviceModelRelation(@Param("domainId") Long domainId, @Param("deviceModelId") Long deviceModelId);

    void deleteDomainDeviceModelRelationsByDomainId(@Param("domainId") Long domainId);
}

