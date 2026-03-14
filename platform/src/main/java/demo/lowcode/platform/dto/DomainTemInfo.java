package demo.lowcode.platform.dto;

import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.entity.DeviceTypeOld;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class DomainTemInfo {
    private NewDomain domainData;
    private List<NewTemplate> templates;
    private List<DeviceType> deviceTypes;
    private List<ComponentDto> components;
}
