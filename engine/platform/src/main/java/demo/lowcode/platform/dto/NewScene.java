package demo.lowcode.platform.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class NewScene {
    private String code;
    private String name;
    private String description;
    private String status;
    private String url;
    private Long domainId;
    private Location location;
}
