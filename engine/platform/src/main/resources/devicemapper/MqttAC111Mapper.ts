import mqtt from 'mqtt';
import { DeviceMapper} from '../device-mapper';
import { ProviderConfig } from '../../domain/provider-config';
import { BaseDeviceModel } from '../../domain/model';

export class MqttAC111Mapper implements DeviceMapper {
  metaModel : BaseDeviceModel;
  deviceModel = 'AC111';
  provider = 'mqtt';
  private client: mqtt.MqttClient;
  private cfg: ProviderConfig;
  propertyMap: Record<string, string> = {
    "hvacMode": "hvacMode",
    "tempTarget": "tempTarget",
    "tempCurrent": "tempCurrent"
  };
  actionMap: Record<string, string> = {
    "setMode": `return 1`,
    "setTemperature": `reutrn 2`
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

  async callAction(actionId: string, params: any): Promise<any> {
    const impl = this.actionMap[actionId];
    if (impl) {
      try {
        // 创建一个函数并执行
        const fn = new Function('params', 'device', impl);
        return await fn(params, this);
      } catch (e) {
        console.error(`Execute action ${actionId} failed:`, e);
        throw e;
      }
    }
    return null;
  }

  match(rawDevice: any): boolean {
    return true;
  }
}
