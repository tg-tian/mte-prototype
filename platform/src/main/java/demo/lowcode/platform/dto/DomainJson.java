package demo.lowcode.platform.dto;

import demo.lowcode.common.Property;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class DomainJson {
    private String domainID;
    private  String domainName;
    private List<Property> domainField;
}

