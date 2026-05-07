package demo.lowcode.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import demo.lowcode.platform.entity.DeviceModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainTemInfo {
    private NewDomain domainData;
    private List<NewTemplate> templates;
    private List<DeviceModel> deviceTypes;
    private List<ComponentDto> components;
}
