package lowcode.device.component.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperConfigDto {
    // maven目录
    public String mavenPath;
    // 设备打包临时目录
    public String projectPath;
    // 工作区目录
    public String workspacePath;
    // 设备定义工作目录
    public String definitionPath;
}
