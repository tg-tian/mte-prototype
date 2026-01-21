import { BaseDeviceModel } from '../domain/model';

export interface DeviceMapper {
  metaModel: BaseDeviceModel;
  deviceModel: string;
  provider: string;
  match(rawDevice: any): boolean;
  mapProperties(rawProps: any): Record<string, any>;
  mapEvent(rawEvent: any): any | null;
  callAction(actionId: string, params: any): Promise<any>;
}
