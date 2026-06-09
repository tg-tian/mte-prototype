package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Scene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SceneMapper extends BaseMapper<Scene> {
    Long getSceneId(@Param("sceneCode") String sceneCode);

    Scene selectBySceneCode(@Param("sceneCode") String sceneCode);

    Scene selectBySceneIdValue(@Param("sceneId") Long sceneId);

    int updateBySceneIdValue(@Param("sceneId") Long sceneId, @Param("scene") Scene scene);

    int deleteBySceneIdValue(@Param("sceneId") Long sceneId);

    List<Scene> selectByDomainId(@Param("domainId") Long domainId);

    List<Scene> selectAll();
}
