import mqtt from 'mqtt';
import { DeviceMapper } from '../device-mapper';
import { ProviderConfig } from '../../domain/provider-config';
import { BaseDeviceModel } from '../../domain/model';

export class MqttConfigurableCameraMapper implements DeviceMapper {
  metaModel: BaseDeviceModel;
  deviceModel = 'CONFIG-CAMERA-01';
  provider = 'mqtt';
  private client: mqtt.MqttClient;
  private cfg: ProviderConfig;

  propertyMap: Record<string, any> = {
    configured: 'configured',
    power_on: 'power_on',
    stream_status: 'stream_status',
    last_seen: 'last_seen'
  };

  eventMap: Record<string, any> = {
    face_detected: {
      _to: 'detected',
      person_id: 'user_id',
      person_name: 'person_name',
      meeting_room: 'meeting_room',
      vip: 'vip',
      timestamp: 'timestamp'
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

  on(deviceId: string, args: any): void {
    const payload = { action: 'on', args: {} }; this.client.publish('devices/' + deviceId + '/command', JSON.stringify(payload));
  }

  off(deviceId: string, args: any): void {
    const payload = { action: 'off', args: {} }; this.client.publish('devices/' + deviceId + '/command', JSON.stringify(payload));
  }

  configure(deviceId: string, args: any): void {
    const payload = { action: 'configureRecognition', args: { url: args.url, apiKey: args.api_key ?? args.apiKey, secretKey: args.secret_key ?? args.secretKey } }; this.client.publish('devices/' + deviceId + '/command', JSON.stringify(payload));
  }
}
