package demo.lowcode.platform.dto;

import demo.lowcode.platform.model.device.Model;
import lombok.Data;

@Data
public class ModelInfo {
    private Long deviceTypeId;
    private Model model;
}
