import mqtt from 'mqtt';
import { DeviceMapper } from '../device-mapper';
import { ProviderConfig } from '../../domain/provider-config';
import { BaseDeviceModel } from '../../domain/model';

export class MqttDeliveryRobotMapper implements DeviceMapper {
  metaModel: BaseDeviceModel;
  deviceModel = 'DeliveryRobot-X1';
  provider = 'mqtt';
  private client: mqtt.MqttClient;
  private cfg: ProviderConfig;

  propertyMap: Record<string, any> = {
    status: 'status',
    batteryLevel: 'batteryLevel'
  };

  eventMap: Record<string, any> = {
    deliveryComplete: {
      _to: 'deliveryComplete',
      order_id: 'order_id',
      finish_time: 'finish_time'
    },
    deliveryFailed: {
      _to: 'deliveryFailed',
      reason: 'reason'
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

  deliverFood(deviceId: string, args: { order_id: string; pickup_location: string; target_location: string }): void {
    const payload = {
      action: 'deliverFood',
      args: {
        order_id: args.order_id,
        pickup_location: args.pickup_location,
        target_location: args.target_location
      }
    };
    this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));
  }
}
