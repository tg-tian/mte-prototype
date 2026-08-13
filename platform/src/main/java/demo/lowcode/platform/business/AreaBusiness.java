package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.platform.dto.NewArea;
import demo.lowcode.platform.entity.Area;
import demo.lowcode.platform.entity.Scene;
import demo.lowcode.platform.mapper.AreaMapper;
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
    public AreaBusiness(AreaMapper areaMapper, SceneMapper sceneMapper) {
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
        Long sceneId = newArea.getSceneId() != null ? newArea.getSceneId() :
                (newArea.getScene() == null ? null : newArea.getScene().getSceneId());
        if (sceneId == null) {
            throw new RuntimeException("关联的场景不存在");
        }
        Scene scene = sceneMapper.selectBySceneIdValue(sceneId);
        if (scene == null) {
            throw new RuntimeException("关联的场景不存在");
        }
        // 插入区域
        Area area = new Area();
        if (newArea.getId() == null || newArea.getId() <= 0) {
            throw new RuntimeException("区域ID不能为空");
        }
        if (areaMapper.selectByAreaIdValue(newArea.getId()) != null) {
            throw new RuntimeException("区域ID已存在");
        }
        area.setId(newArea.getId());
        area.setDescription(newArea.getDescription());
        area.setName(newArea.getName());
        area.setImage(newArea.getImage());
        area.setPolygon(newArea.getPolygon());
        area.setParentId(newArea.getParentId());
        area.setSceneId(sceneId);
        areaMapper.insert(area);
        return area;
    }

    /**
     * 更新区域信息
     */
    public Area updateArea(Long id, NewArea updatedArea) {
        // 检查区域是否存在（id 参数是逻辑ID area_id）
        Area existingArea = areaMapper.selectByAreaIdValue(id);
        if (existingArea == null) {
            throw new RuntimeException("区域不存在");
        }

        // 验证关联的场景是否存在
        if (updatedArea.getSceneId() != null) {
            Scene scene = sceneMapper.selectBySceneIdValue(updatedArea.getSceneId());
            if (scene == null) {
                throw new RuntimeException("关联的场景不存在");
            }
        }

        // 更新区域信息
        existingArea.setName(updatedArea.getName());
        existingArea.setDescription(updatedArea.getDescription());
        existingArea.setImage(updatedArea.getImage());
        existingArea.setParentId(updatedArea.getParentId());
        existingArea.setPolygon(updatedArea.getPolygon());
        existingArea.setSceneId(updatedArea.getSceneId());
        areaMapper.updateByAreaIdValue(existingArea.getId(), existingArea);
        return existingArea;
    }

    /**
     * 删除区域
     */
    public void deleteAreaById(Long id) {
        Area area = areaMapper.selectByAreaIdValue(id);
        if (area == null) {
            throw new RuntimeException("区域不存在");
        }

        List<Area> areas = areaMapper.selectByParentIdValue(area.getId());
        List<Long> ids = areas.stream().map(Area::getId).collect(Collectors.toList());
        if (ids.size() > 0){
            areaMapper.updateParentByAreaIds(-1L, ids);
        }

        areaMapper.deleteByAreaIdValue(id);
    }


    public void addChildren(Long parentId, List<Long> childrenIds) {
        Area area = areaMapper.selectByAreaIdValue(parentId);
        if (area == null) {
            throw new RuntimeException("区域不存在");
        }
        areaMapper.updateParentByAreaIds(parentId, childrenIds);
    }

    public NewArea buildAreaTree(Long sceneId, Long areaId) {
        List<Area> areas = areaMapper.selectBySceneId(sceneId);
        Area area = areaMapper.selectByAreaIdValue(areaId);
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
        areaMapper.deleteParentByAreaId(id);
    }
}
