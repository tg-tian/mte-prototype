package demo.lowcode.platform.controller;

import demo.lowcode.platform.dto.ComponentDto;
import demo.lowcode.platform.service.ComponentService;
import demo.lowcode.platform.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/components")
public class ComponentController {

    private final ComponentService componentService;

    @Autowired
    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping
    public Result<List<ComponentDto>> getAllComponents() {
        try {
            List<ComponentDto> components = componentService.getAllComponents();
            return Result.success(components);
        } catch (Exception e) {
            return Result.fail("Failed to fetch components: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<ComponentDto> getComponentById(@PathVariable Long id) {
        try {
            ComponentDto component = componentService.getComponentById(id);
            if (component == null) {
                return Result.fail("Component not found");
            }
            return Result.success(component);
        } catch (Exception e) {
            return Result.fail("Failed to fetch component: " + e.getMessage());
        }
    }

    @PostMapping
    public Result<ComponentDto> createComponent(@RequestBody ComponentDto componentDto) {
        try {
            ComponentDto createdComponent = componentService.createComponent(componentDto);
            return Result.success(createdComponent);
        } catch (Exception e) {
            return Result.fail("Failed to create component: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<ComponentDto> updateComponent(@PathVariable Long id, @RequestBody ComponentDto componentDto) {
        try {
            ComponentDto updatedComponent = componentService.updateComponent(id, componentDto);
            if (updatedComponent == null) {
                return Result.fail("Component not found");
            }
            return Result.success(updatedComponent);
        } catch (Exception e) {
            return Result.fail("Failed to update component: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteComponent(@PathVariable Long id) {
        try {
            boolean success = componentService.deleteComponent(id);
            if (!success) {
                return Result.fail("Component not found or could not be deleted");
            }
            return Result.success(true);
        } catch (Exception e) {
            return Result.fail("Failed to delete component: " + e.getMessage());
        }
    }
}
