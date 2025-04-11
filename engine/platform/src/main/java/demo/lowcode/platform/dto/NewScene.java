package demo.lowcode.platform.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class NewScene {
    private String name;
    private String description;
    private String status;
    private Long domainId;
    private Location location;
}
