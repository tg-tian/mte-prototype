import mqtt from 'mqtt';
import { DeviceMapper } from '../device-mapper';
import { ProviderConfig } from '../../domain/provider-config';
import { BaseDeviceModel } from '../../domain/model';

export class MqttACMapper implements DeviceMapper {
  metaModel: BaseDeviceModel;
  deviceModel = 'HAIER-AC-1001';
  provider = 'mqtt';
  private client: mqtt.MqttClient;
  private cfg: ProviderConfig;

  propertyMap: Record<string, any> = {
    current_temperature: 'tempCurrent',
    temperature: 'tempTarget',
    hvac_mode: {
      _to: 'hvacMode',
      _map: {
        0: 'cool',
        1: 'heat',
        2: 'fan',
        3: 'dry',
        4: 'auto'
      }
    }
  };

  eventMap: Record<string, any> = {
    sys_error: {
      _to: 'error',
      code: 'code',
      msg: 'message'
    },
    filter_warn: {
      _to: 'filterAlert',
      op_hours: 'hoursUsed'
    },
    comp_status: {
      _to: 'compressorStatus',
      val: {
        _to: 'status',
        _map: {
          0: 'off',
          1: 'on',
          2: 'defrosting'
        }
      }
    }
  };

  constructor(config: ProviderConfig, metaModel: BaseDeviceModel) {
    this.cfg = config;
    this.metaModel = metaModel;
    this.client = mqtt.connect(this.cfg.communication.baseUrl);
  }

  private isPlainObject(value: any): boolean {
    return value !== null && typeof value === 'object' && !Array.isArray(value);
  }

  private hasNestedMapping(mapping: any): boolean {
    return Object.keys(mapping).some(key => !key.startsWith('_'));
  }

  private processMapping(sourceData: any, mapping: any): Record<string, any> {
    const mappedData: Record<string, any> = {};
    if (!this.isPlainObject(sourceData) || !this.isPlainObject(mapping)) {
      return mappedData;
    }
    for (const [key, value] of Object.entries(sourceData)) {
      const fieldMapping = mapping[key];
      if (!fieldMapping) {
        mappedData[key] = value;
        continue;
      }
      if (typeof fieldMapping === 'string') {
        mappedData[fieldMapping] = value;
        continue;
      }
      if (!this.isPlainObject(fieldMapping)) {
        mappedData[key] = value;
        continue;
      }
      const targetKey = fieldMapping._to || key;
      if (fieldMapping._map) {
        mappedData[targetKey] = fieldMapping._map[value as any] ?? value;
        continue;
      }
      if (this.isPlainObject(value) && this.hasNestedMapping(fieldMapping)) {
        mappedData[targetKey] = this.processMapping(value, fieldMapping);
      } else {
        mappedData[targetKey] = value;
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

  setMode(deviceId: string, args: any): void {
    const modeMap: Record<string, number> = {
      'cool': 0,
      'heat': 1,
      'fan': 2,
      'dry': 3,
      'auto': 4
    };
    const modeVal = modeMap[args.mode];
    const payload = { action: 'setMode', args: { mode: modeVal } };
    this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));
  }

  setTemperature(deviceId: string, args: any): void {
    const payload = { action: 'setTemperature', args };
    this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));
  }
}
