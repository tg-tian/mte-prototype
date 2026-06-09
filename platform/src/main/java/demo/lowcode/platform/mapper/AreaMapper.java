package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {
    List<Area> selectBySceneId(Long sceneId);
    Area selectByAreaIdValue(@Param("areaId") Long areaId);
    int updateByAreaIdValue(@Param("areaId") Long areaId, @Param("area") Area area);
    int deleteByAreaIdValue(@Param("areaId") Long areaId);
    void updateParentByAreaIds(@Param("parentId") Long parentId, @Param("children") List<Long> childrenAreaIds);
    void deleteParentByAreaId(@Param("areaId") Long areaId);
    List<Area> selectByParentIdValue(@Param("parentId") Long parentId);
}
