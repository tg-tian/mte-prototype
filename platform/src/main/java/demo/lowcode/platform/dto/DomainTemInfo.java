package demo.lowcode.platform.dto;

import demo.lowcode.platform.entity.DeviceModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class DomainTemInfo {
    private NewDomain domainData;
    private List<NewTemplate> templates;
    private List<DeviceModel> deviceTypes;
    private List<ComponentDto> components;
}
