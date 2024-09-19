package lowcode.device.component.controller;

import demo.lowcode.common.CommonConfig;
import lowcode.device.component.dto.DeveloperConfigDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class DeveloperConfigController {
    @GetMapping(value = "/setting/developer")
    public ResponseEntity<?> getDeveloperSetting() {
        DeveloperConfigDto dto = new DeveloperConfigDto(CommonConfig.getMavenPath(), CommonConfig.getProjectPath(), CommonConfig.getWorkspacePath(), CommonConfig.getDefinitionPath());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/setting/developer/edit")
    public ResponseEntity<?> editDeveloperSetting(@RequestBody DeveloperConfigDto dto) {
        CommonConfig.setMavenPath(dto.getMavenPath());
        CommonConfig.setWorkspacePath(dto.getWorkspacePath());
        CommonConfig.setProjectPath(dto.getProjectPath());
        CommonConfig.setDefinitionPath(dto.getDefinitionPath());
        return new ResponseEntity<>("设置成功", HttpStatus.OK);
    }
}
