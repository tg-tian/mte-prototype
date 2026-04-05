package demo.lowcode.platform.business.mappergenerator;

import demo.lowcode.platform.entity.Device;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MqttDeviceMapperGenerator implements DeviceMapperGenerator {
    @Override
    public String getProvider() {
        return "mqtt";
    }

    @Override
    public String generateContent(Device device, String className) {
        String propertyMapStr = toStringMapLiteral(device.getPropertyMap());
        String actionMapStr = toActionMapLiteral(device.getActionMap());
        String eventMapStr = toStringMapLiteral(device.getEventMap());
        String actionFuncsStr = toActionFunctions(device.getActionMap());

        return """
                import mqtt from 'mqtt';
                import { DeviceMapper} from '../device-mapper';
                import { ProviderConfig } from '../../domain/provider-config';
                import { BaseDeviceModel } from '../../domain/model';

                export class %s implements DeviceMapper {
                  metaModel : BaseDeviceModel;
                  deviceModel = '%s';
                  provider = '%s';
                  private client: mqtt.MqttClient;
                  private cfg: ProviderConfig;
                  propertyMap: Record<string, string> = %s;
                  actionMap: Record<string, string> = %s;
                  eventMap: Record<string, string> = %s;

                  constructor(config: ProviderConfig , metaModel: BaseDeviceModel) {
                    this.cfg = config;
                    this.metaModel = metaModel;
                    this.client = mqtt.connect(this.cfg.communication.baseUrl);
                  }

                  mapProperties(rawProps: any): Record<string, any> {
                    const mapped: Record<string, any> = {};
                    for (const [key, value] of Object.entries(rawProps)) {
                      const target = this.propertyMap[key as keyof typeof this.propertyMap];
                      if (target) {
                        mapped[target] = value;
                      } else {
                        mapped[key] = value;
                      }
                    }
                    return mapped;
                  }

                  mapEvent(rawEvent: any): any | null {
                    if (!rawEvent) return null;
                    const eventName = rawEvent.name || rawEvent.event;
                    if (!eventName) return rawEvent;
                    
                    const targetEvent = this.eventMap[eventName as keyof typeof this.eventMap];
                    if (targetEvent) {
                      return {
                        ...rawEvent,
                        name: targetEvent,
                        event: targetEvent
                      };
                    }
                    return rawEvent;
                  }

                  async callAction(actionId: string, params: any): Promise<any> {
                    const impl = this.actionMap[actionId];
                    if (impl) {
                      try {
                        const fn = new Function('params', 'device', impl);
                        return await fn(params, this);
                      } catch (e) {
                        console.error(`Execute action ${actionId} failed:`, e);
                        throw e;
                      }
                    }
                    return null;
                  }
                  
%s

                  match(rawDevice: any): boolean {
                    return true;
                  }
                }
                """.formatted(className, device.getDeviceId(), device.getProvider(), propertyMapStr, actionMapStr, eventMapStr, actionFuncsStr);
    }

    private String toStringMapLiteral(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        return "{\n" + map.entrySet().stream()
                .map(e -> "    \"" + e.getKey() + "\": \"" + e.getValue() + "\"")
                .collect(Collectors.joining(",\n")) + "\n  }";
    }

    private String toActionMapLiteral(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        return "{\n" + map.entrySet().stream()
                .map(e -> "    \"" + e.getKey() + "\": `" + e.getValue().replace("`", "\\`") + "`")
                .collect(Collectors.joining(",\n")) + "\n  }";
    }

    private String toActionFunctions(Map<String, String> actionMap) {
        if (actionMap == null || actionMap.isEmpty()) {
            return "";
        }
        return actionMap.entrySet().stream()
                .map(e -> "                  async " + e.getKey() + "(params: any): Promise<any> {\n" +
                        "                    const impl = this.actionMap['" + e.getKey() + "'];\n" +
                        "                    if (impl) {\n" +
                        "                      try {\n" +
                        "                        const fn = new Function('params', 'device', impl);\n" +
                        "                        return await fn(params, this);\n" +
                        "                      } catch (e) {\n" +
                        "                        console.error(`Execute action " + e.getKey() + " failed:`, e);\n" +
                        "                        throw e;\n" +
                        "                      }\n" +
                        "                    }\n" +
                        "                    return null;\n" +
                        "                  }")
                .collect(Collectors.joining("\n\n"));
    }
}
