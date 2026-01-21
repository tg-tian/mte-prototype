package demo.lowcode.platform.model.devicev1;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

@Data
public class ActionDefinition implements Serializable {
  private Map<String, PropertyDefinition> arguments;
  private String description;
}
