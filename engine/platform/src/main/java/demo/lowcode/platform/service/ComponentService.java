package demo.lowcode.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import demo.lowcode.platform.dto.ComponentDto;
import demo.lowcode.platform.entity.Component;

import java.util.List;

public interface ComponentService extends IService<Component> {

    /**
     * Get all components
     * @return list of components
     */
    List<ComponentDto> getAllComponents();

    /**
     * Get component by ID
     * @param id component ID
     * @return component data
     */
    ComponentDto getComponentById(Long id);

    /**
     * Create a new component
     * @param componentDto component data
     * @return created component
     */
    ComponentDto createComponent(ComponentDto componentDto);

    /**
     * Update an existing component
     * @param id component ID
     * @param componentDto component data
     * @return updated component
     */
    ComponentDto updateComponent(Long id, ComponentDto componentDto);

    /**
     * Delete a component
     * @param id component ID
     * @return true if successful
     */
    boolean deleteComponent(Long id);

    void bindDomainAndComponent(Long domainId, Long componentId);

    void unbindDomainAndComponent(Long domainId, Long componentId);

    List<ComponentDto> getComponentListByDomain(Long domainId);
}
