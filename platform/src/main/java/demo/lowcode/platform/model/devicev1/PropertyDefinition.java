package demo.lowcode.platform.model.devicev1;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PropertyDefinition implements Serializable {
  private String type; // "string" | "number" | "boolean" | "enum" | "object" | "array"
  private String unit;
  private Boolean readOnly;
  private Double min;
  private Double max;
  private List<String> enumValues;
  private String description;
}
