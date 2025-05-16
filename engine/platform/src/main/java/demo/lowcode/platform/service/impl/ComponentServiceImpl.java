package demo.lowcode.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.platform.dto.ComponentDto;
import demo.lowcode.platform.entity.Component;
import demo.lowcode.platform.mapper.ComponentMapper;
import demo.lowcode.platform.service.ComponentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component> implements ComponentService {

    private final ObjectMapper objectMapper;
    private final SimpleDateFormat dateFormat;

    public ComponentServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public List<ComponentDto> getAllComponents() {
        List<Component> components = this.list();
        List<ComponentDto> result = new ArrayList<>();
        
        for (Component component : components) {
            result.add(convertToDto(component));
        }
        
        return result;
    }

    @Override
    public ComponentDto getComponentById(Long id) {
        Component component = this.getById(id);
        if (component == null) {
            return null;
        }
        return convertToDto(component);
    }

    @Override
    public ComponentDto createComponent(ComponentDto componentDto) {
        Component component = new Component();
        component.setComponentCode(componentDto.getCode());
        component.setComponentName(componentDto.getName());
        component.setComponentDescription(componentDto.getDescription());
        component.setComponentType(componentDto.getType());
        component.setComponentPurpose(componentDto.getPurpose());
        
        // Convert constraints to JSON
        try {
            Map<String, Object> constraintsMap = new HashMap<>();
            
            if ("node".equals(componentDto.getType())) {
                constraintsMap.put("inputConstraint", componentDto.getInputConstraint());
                constraintsMap.put("outputConstraint", componentDto.getOutputConstraint());
            } else {
                constraintsMap.put("startConstraint", componentDto.getStartConstraint());
                constraintsMap.put("endConstraint", componentDto.getEndConstraint());
            }
            
            component.setConstraints(objectMapper.writeValueAsString(constraintsMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting constraints to JSON", e);
        }
        
        component.setCreateTime(new Date());
        
        // Save to database
        this.save(component);
        
        return getComponentById(component.getComponentId());
    }

    @Override
    public ComponentDto updateComponent(Long id, ComponentDto componentDto) {
        Component component = this.getById(id);
        if (component == null) {
            return null;
        }
        
        component.setComponentCode(componentDto.getCode());
        component.setComponentName(componentDto.getName());
        component.setComponentDescription(componentDto.getDescription());
        component.setComponentType(componentDto.getType());
        component.setComponentPurpose(componentDto.getPurpose());
        
        // Convert constraints to JSON
        try {
            Map<String, Object> constraintsMap = new HashMap<>();
            
            if ("node".equals(componentDto.getType())) {
                constraintsMap.put("inputConstraint", componentDto.getInputConstraint());
                constraintsMap.put("outputConstraint", componentDto.getOutputConstraint());
            } else {
                constraintsMap.put("startConstraint", componentDto.getStartConstraint());
                constraintsMap.put("endConstraint", componentDto.getEndConstraint());
            }
            
            component.setConstraints(objectMapper.writeValueAsString(constraintsMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting constraints to JSON", e);
        }
        
        component.setUpdateTime(new Date());
        
        // Update in database
        this.updateById(component);
        
        return getComponentById(id);
    }

    @Override
    public boolean deleteComponent(Long id) {
        return this.removeById(id);
    }
    
    /**
     * Convert entity to DTO
     */
    private ComponentDto convertToDto(Component component) {
        ComponentDto dto = new ComponentDto();
        dto.setId(component.getComponentId());
        dto.setCode(component.getComponentCode());
        dto.setName(component.getComponentName());
        dto.setDescription(component.getComponentDescription());
        dto.setType(component.getComponentType());
        dto.setPurpose(component.getComponentPurpose());
        
        // Format dates
        if (component.getCreateTime() != null) {
            dto.setCreateTime(dateFormat.format(component.getCreateTime()));
        }
        if (component.getUpdateTime() != null) {
            dto.setUpdateTime(dateFormat.format(component.getUpdateTime()));
        }
        
        // Parse constraints from JSON
        try {
            if (component.getConstraints() != null) {
                Map<String, Object> constraintsMap = objectMapper.readValue(component.getConstraints(), Map.class);
                
                if ("node".equals(component.getComponentType())) {
                    if (constraintsMap.containsKey("inputConstraint")) {
                        dto.setInputConstraint(objectMapper.convertValue(constraintsMap.get("inputConstraint"), 
                                demo.lowcode.platform.dto.ConstraintDto.class));
                    }
                    if (constraintsMap.containsKey("outputConstraint")) {
                        dto.setOutputConstraint(objectMapper.convertValue(constraintsMap.get("outputConstraint"), 
                                demo.lowcode.platform.dto.ConstraintDto.class));
                    }
                } else {
                    if (constraintsMap.containsKey("startConstraint")) {
                        dto.setStartConstraint(objectMapper.convertValue(constraintsMap.get("startConstraint"), 
                                demo.lowcode.platform.dto.ConstraintDto.class));
                    }
                    if (constraintsMap.containsKey("endConstraint")) {
                        dto.setEndConstraint(objectMapper.convertValue(constraintsMap.get("endConstraint"), 
                                demo.lowcode.platform.dto.ConstraintDto.class));
                    }
                }
            }
        } catch (JsonProcessingException e) {
            // Log error but don't fail
            e.printStackTrace();
        }
        
        return dto;
    }
}
