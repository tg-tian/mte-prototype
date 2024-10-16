package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.Domain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SceneMapper extends BaseMapper<Domain> {
    public Long getSceneId(@Param("sceneName") String sceneName);
}
