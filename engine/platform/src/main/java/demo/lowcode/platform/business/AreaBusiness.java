package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.platform.dto.NewArea;
import demo.lowcode.platform.entity.Area;
import demo.lowcode.platform.entity.Scene;
import demo.lowcode.platform.mapper.AreaMapper;
import demo.lowcode.platform.mapper.DeviceMapper;
import demo.lowcode.platform.mapper.DeviceTypeMapper;
import demo.lowcode.platform.mapper.SceneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Area createArea(NewArea newArea) {
        // 验证关联的场景是否存在
        Scene scene = sceneMapper.selectById(newArea.getSceneId());
        if (scene == null) {
            throw new RuntimeException("关联的场景不存在");
        }
        // 插入区域
        Area area = new Area();
        area.setDescription(newArea.getDescription());
        area.setName(newArea.getName());
        area.setImage(newArea.getImage());
        area.setPosition(newArea.getPosition());
        area.setParentId(newArea.getParentId());
        area.setSceneId(newArea.getSceneId());
        areaMapper.insert(area);
        return area;
    }

    /**
     * 更新区域信息
     */
    public Area updateArea(Long id, NewArea updatedArea) {
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
        existingArea.setName(updatedArea.getName());
        existingArea.setDescription(updatedArea.getDescription());
        existingArea.setImage(updatedArea.getImage());
        existingArea.setParentId(updatedArea.getParentId());
        existingArea.setPosition(updatedArea.getPosition());
        existingArea.setSceneId(updatedArea.getSceneId());
        areaMapper.updateById(existingArea);
        return existingArea;
    }

    /**
     * 删除区域
     */
    public void deleteAreaById(Long id) {
        Area area = areaMapper.selectById(id);
        if (area == null) {
            throw new RuntimeException("区域不存在");
        }

        List<Area> areas = areaMapper.selectByParentId(area.getId());
        List<Long> ids = areas.stream().map(Area::getId).collect(Collectors.toList());
        if (ids.size() > 0){
            areaMapper.updateParent(-1L,ids);
        }

        areaMapper.deleteById(id);
    }


    public void addChildren(Long parentId, List<Long> childrenIds) {
        Area area = areaMapper.selectById(parentId);
        if (area == null) {
            throw new RuntimeException("区域不存在");
        }
        areaMapper.updateParent(parentId,childrenIds);
    }

    public NewArea buildAreaTree(Long sceneId, Long areaId) {
        List<Area> areas = areaMapper.selectBySceneId(sceneId);
        Area area = areaMapper.selectById(areaId);
        Area area1 = findRoot(area, areas);
        ObjectMapper objectMapper = new ObjectMapper();
        NewArea root = objectMapper.convertValue(area1, NewArea.class);
        buildChild(root,areas);
        return root;
    }

    private Area findRoot(Area node, List<Area> areas) {
        while(node != null && node.getParentId() != -1){
            for (Area area : areas) {
                if (area.getId().equals(node.getParentId())) {
                    node = area;
                    break;
                }
            }
        }
        return node;
    }

    private void buildChild(NewArea root, List<Area> areas) {
        for (Area area : areas) {
            Long parentId = area.getParentId();
            if (root.getId() == parentId) {
                ObjectMapper objectMapper = new ObjectMapper();
                NewArea newArea = objectMapper.convertValue(area, NewArea.class);
                if(root.getChildren() == null) {
                    List<NewArea> newAreas = new ArrayList<>();
                    newAreas.add(newArea);
                    root.setChildren(newAreas);
                }else{
                    root.getChildren().add(newArea);
                }
                buildChild(newArea, areas);
            }
        }
    }

    public void deleteParent(Long id) {
        areaMapper.deleteParent(id);
    }
}
