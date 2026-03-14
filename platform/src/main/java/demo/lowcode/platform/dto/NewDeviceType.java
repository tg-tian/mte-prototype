package demo.lowcode.platform.dto;

import demo.lowcode.platform.model.device.Model;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class NewDeviceType {
    private String code;
    private String name;
    private String description;
    @Nullable
    private Model model;
}
