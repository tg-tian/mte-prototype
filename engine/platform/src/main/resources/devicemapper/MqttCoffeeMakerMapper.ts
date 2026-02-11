import mqtt from 'mqtt';
import { DeviceMapper } from '../device-mapper';
import { ProviderConfig } from '../../domain/provider-config';
import { BaseDeviceModel } from '../../domain/model';

export class MqttCoffeeMakerMapper implements DeviceMapper {
  metaModel: BaseDeviceModel;
  deviceModel = 'DELONGHI-CF-2001';
  provider = 'mqtt';
  private client: mqtt.MqttClient;
  private cfg: ProviderConfig;

  propertyMap: Record<string, any> = {
    water_temperature: 'waterTemperature',
    water_level: 'waterLevel',
    status: 'status'
  };

  eventMap: Record<string, any> = {
    coffeeComplete: {
      _to: 'coffeeComplete',
      coffee_type: 'coffee_type',
      duration: 'duration',
      start_time: 'start_time'
    }
  };

  constructor(config: ProviderConfig, metaModel: BaseDeviceModel) {
    this.cfg = config;
    this.metaModel = metaModel;
    this.client = mqtt.connect(this.cfg.communication.baseUrl);
  }

  private processMapping(sourceData: any, mapping: any): Record<string, any> {
    const mappedData: Record<string, any> = {};
    for (const [key, value] of Object.entries(sourceData)) {
      const fieldMapping = mapping[key];
      if (!fieldMapping) {
        mappedData[key] = value;
        continue;
      }
      if (typeof fieldMapping === 'string') {
        mappedData[fieldMapping] = value;
      } else if (typeof fieldMapping === 'object') {
        const targetKey = fieldMapping._to;
        if (fieldMapping._map) {
          mappedData[targetKey] = fieldMapping._map[value as any] ?? value;
        } else {
          mappedData[targetKey] = value;
        }
      }
    }
    return mappedData;
  }

  mapProperties(rawProps: any): Record<string, any> {
    return this.processMapping(rawProps, this.propertyMap);
  }

  mapEvents(rawEvent: any): Record<string, any> {
    const result: Record<string, any> = {};
    for (const [key, value] of Object.entries(rawEvent)) {
      if (this.eventMap[key]) {
        const mapping = this.eventMap[key];
        const targetEventName = mapping._to || key;
        result[targetEventName] = this.processMapping(value, mapping);
      }
    }
    return result;
  }

  makeCoffee(deviceId: string, args: { coffee_type: string }): void {
    const payload = { 
      action: 'makeCoffee', 
      args: { 
        coffee_type: args.coffee_type 
      } 
    };
    this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));
  }
}
