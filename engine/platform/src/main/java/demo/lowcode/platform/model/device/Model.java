package demo.lowcode.platform.model.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model implements Serializable {
    private List<Property> properties;
    private List<Service> services;
    private List<Event> events;
}
