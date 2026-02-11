package demo.lowcode.platform.model.devicev1;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

@Data
public class EventDefinition implements Serializable {
  private Map<String, PropertyDefinition> fields;
  private Map<String, PropertyDefinition> outputs;
  private String level; // "info" | "warning" | "error"
  private String description;
}
