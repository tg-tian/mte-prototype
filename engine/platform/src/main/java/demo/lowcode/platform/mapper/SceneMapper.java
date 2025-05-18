package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Scene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SceneMapper extends BaseMapper<Scene> {
    Long getSceneId(@Param("sceneCode") String sceneCode);

    List<Scene> selectByDomainId(@Param("domainId") Long domainId);

    Scene selectBySceneCode(@Param("sceneCode") String sceneCode);
}
