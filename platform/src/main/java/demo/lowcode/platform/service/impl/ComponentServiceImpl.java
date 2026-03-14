package demo.lowcode.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.platform.dto.ComponentDto;
import demo.lowcode.platform.entity.Component;
import demo.lowcode.platform.entity.DomainComponent;
import demo.lowcode.platform.mapper.ComponentMapper;
import demo.lowcode.platform.mapper.DomainComponentMapper;
import demo.lowcode.platform.service.ComponentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component> implements ComponentService {

    private final ObjectMapper objectMapper;
    private final SimpleDateFormat dateFormat;
    private final ComponentMapper componentMapper;
    private final DomainComponentMapper domainComponentMapper;

    @Autowired
    public ComponentServiceImpl(ComponentMapper componentMapper, DomainComponentMapper domainComponentMapper) {
        this.objectMapper = new ObjectMapper();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.componentMapper = componentMapper;
        this.domainComponentMapper = domainComponentMapper;
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

            // Convert properties, inputs, outputs to JSON
            if (componentDto.getProperties() != null) {
                component.setProperties(objectMapper.writeValueAsString(componentDto.getProperties()));
            }
            if (componentDto.getInputs() != null) {
                component.setInputs(objectMapper.writeValueAsString(componentDto.getInputs()));
            }
            if (componentDto.getOutputs() != null) {
                component.setOutputs(objectMapper.writeValueAsString(componentDto.getOutputs()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting data to JSON", e);
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

            // Convert properties, inputs, outputs to JSON
            if (componentDto.getProperties() != null) {
                component.setProperties(objectMapper.writeValueAsString(componentDto.getProperties()));
            }
            if (componentDto.getInputs() != null) {
                component.setInputs(objectMapper.writeValueAsString(componentDto.getInputs()));
            }
            if (componentDto.getOutputs() != null) {
                component.setOutputs(objectMapper.writeValueAsString(componentDto.getOutputs()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting data to JSON", e);
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

    @Override
    public void bindDomainAndComponent(Long domainId, Long componentId) {
        if (domainComponentMapper.selectByDomainAndComponent(domainId, componentId) != null){
            throw new RuntimeException("绑定失败：该组件已绑定");
        }
        DomainComponent component = new DomainComponent();
        component.setDomainId(domainId);
        component.setComponentId(componentId);
        component.setComponentType("component");
        domainComponentMapper.insert(component);
    }

    @Override
    public void unbindDomainAndComponent(Long domainId, Long componentId) {
        DomainComponent domainComponent = domainComponentMapper.selectByDomainAndComponent(domainId, componentId);
        if (domainComponent == null){
            throw new RuntimeException("两者未绑定");
        }
        domainComponentMapper.deleteById(domainComponent.getId());
    }

    @Override
    public List<ComponentDto> getComponentListByDomain(Long domainId) {
        List<ComponentDto> result = new ArrayList<>();
        List<Component> components = componentMapper.selectByDomainId(domainId);
        for (Component component: components) {
            result.add(convertToDto(component));
        }
        return result;
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

            // Parse properties, inputs, outputs from JSON
            if (component.getProperties() != null && !component.getProperties().isEmpty()) {
                dto.setProperties(objectMapper.readValue(component.getProperties(), Map.class));
            }
            if (component.getInputs() != null && !component.getInputs().isEmpty()) {
                dto.setInputs(objectMapper.readValue(component.getInputs(), List.class));
            }
            if (component.getOutputs() != null && !component.getOutputs().isEmpty()) {
                dto.setOutputs(objectMapper.readValue(component.getOutputs(), List.class));
            }
        } catch (JsonProcessingException e) {
            // Log error but don't fail
            e.printStackTrace();
        }

        return dto;
    }
}
