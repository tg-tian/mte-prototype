package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {
    List<Area> selectBySceneId(Long sceneId);
    Area selectById(Long id);
    void updateParent(Long id, List<Long> children);
    void deleteParent(Long id);

    List<Area> selectByParentId(Long id);
}
