package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.lowcode.platform.dto.NewDevice;
import demo.lowcode.platform.entity.Area;
import demo.lowcode.platform.entity.Device;
import demo.lowcode.platform.entity.DeviceOld;
import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.entity.Scene;
import demo.lowcode.platform.mapper.AreaMapper;
import demo.lowcode.platform.mapper.DeviceMapper;
import demo.lowcode.platform.mapper.DeviceOldMapper;
import demo.lowcode.platform.mapper.DeviceTypeMapper;
import demo.lowcode.platform.mapper.SceneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AreaBusiness extends ServiceImpl<AreaMapper, Area> implements IService<Area> {
    private final AreaMapper areaMapper;
    private final SceneMapper sceneMapper;

    @Autowired
    public AreaBusiness(AreaMapper areaMapper, DeviceMapper deviceMapper, DeviceTypeMapper deviceTypeMapper, SceneMapper sceneMapper) {
        this.areaMapper = areaMapper;
        this.sceneMapper = sceneMapper;
    }

    /**
     * 获取指定场景下的区域列表
     */
    public List<Area> getAreaListByScene(Long sceneId) {
        return areaMapper.selectBySceneId(sceneId);
    }

    /**
     * 创建新的区域
     */
    public Area createArea(Area newArea) {
        // 验证关联的场景是否存在
        Scene scene = sceneMapper.selectById(newArea.getSceneId());
        if (scene == null) {
            throw new RuntimeException("关联的场景不存在");
        }

        // 插入区域
        areaMapper.insert(newArea);
        return newArea;
    }

    /**
     * 更新区域信息
     */
    public Area updateArea(Long id, Area updatedArea) {
        // 检查区域是否存在
        Area existingArea = areaMapper.selectById(id);
        if (existingArea == null) {
            throw new RuntimeException("区域不存在");
        }

        // 验证关联的场景是否存在
        if (updatedArea.getSceneId() != null) {
            Scene scene = sceneMapper.selectById(updatedArea.getSceneId());
            if (scene == null) {
                throw new RuntimeException("关联的场景不存在");
            }
        }

        // 更新区域信息
        updatedArea.setId(id);
        areaMapper.updateById(updatedArea);
        return updatedArea;
    }

    /**
     * 删除区域
     */
    public void deleteAreaById(Long id) {
        Area area = areaMapper.selectById(id);
        if (area == null) {
            throw new RuntimeException("区域不存在");
        }

        areaMapper.deleteById(id);
    }
}