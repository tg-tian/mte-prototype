package demo.lowcode.platform.controller;

import demo.lowcode.platform.dto.ComponentDto;
import demo.lowcode.platform.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/components")
public class ComponentController {

    private final ComponentService componentService;

    @Autowired
    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping
    public ResponseEntity<?> getAllComponents() {
        try {
            List<ComponentDto> components = componentService.getAllComponents();
            return new ResponseEntity<>(components, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch components: " + e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComponentById(@PathVariable Long id) {
        try {
            ComponentDto component = componentService.getComponentById(id);
            if (component == null) {
                throw new RuntimeException("Component not found");
            }
            return new ResponseEntity<>(component, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch components: " + e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<?> createComponent(@RequestBody ComponentDto componentDto) {
        try {
            ComponentDto createdComponent = componentService.createComponent(componentDto);
            return new ResponseEntity<>(createdComponent, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create component: " + e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComponent(@PathVariable Long id, @RequestBody ComponentDto componentDto) {
        try {
            ComponentDto updatedComponent = componentService.updateComponent(id, componentDto);
            if (updatedComponent == null) {
                throw new RuntimeException("Component not found");
            }
            return new ResponseEntity<>(updatedComponent, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update component: " + e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComponent(@PathVariable Long id) {
        try {
            boolean success = componentService.deleteComponent(id);
            if (!success) {
                throw new RuntimeException("Component not found or could not be deleted");
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete component: " + e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
