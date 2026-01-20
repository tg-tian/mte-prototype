import mqtt from 'mqtt';
import { DeviceMapper} from '../device-mapper';
import { ProviderConfig } from '../../domain/provider-config';
import { BaseDeviceModel } from '../../domain/model';

export class MqttACMapper implements DeviceMapper {
  metaModel : BaseDeviceModel;
  deviceModel = 'HAIER-AC-1001';
  provider = 'mqtt';
  private client: mqtt.MqttClient;
  private cfg: ProviderConfig;
  propertyMap: Record<string, string> = {
    current_temperature: 'tempCurrent',
    temperature: 'tempTarget',
    hvac_mode: 'hvacMode',
  };

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
    return null;
  }
  setMode(deviceId: string, args: { mode: string }): void {
    const payload = { action: 'setMode', args };
    this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));
  }
  setTemperature(deviceId: string, args: { temp: number }): void {
    const payload = { action: 'setTemperature', args };
    this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));
  }
}
