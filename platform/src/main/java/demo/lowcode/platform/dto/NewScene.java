package demo.lowcode.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewScene {
    private String code;
    private String name;
    private String description;
    private String status;
    private String url;
    private Long domainId;
    private Location location;
    private String imageUrl;
    private String imageRef;
}
