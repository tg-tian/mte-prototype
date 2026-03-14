package demo.lowcode.platform.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Location {
    private Float lng;
    private Float lat;
}
