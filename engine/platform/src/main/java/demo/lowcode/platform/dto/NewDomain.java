package demo.lowcode.platform.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class NewDomain {
    private String name;
    private String description;
    private String status;
}
