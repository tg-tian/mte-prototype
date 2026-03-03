package demo.lowcode.platform.model.devicev1;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

@Data
public class BaseDeviceModel implements Serializable {
  private String modelId;
  private String modelName;
  private String provider;
  private String category;

  private Map<String, PropertyDefinition> properties;
  private Map<String, ActionDefinition> actions;
  private Map<String, EventDefinition> events;

  private Extensions extensions;

  @Data
  public static class Extensions implements Serializable {
    private Object rawModel;
    private Object extraMeta;
  }
}
