import { BaseDeviceModel } from '../domain/model';

export interface DeviceMapper {
  metaModel: BaseDeviceModel;
  deviceModel: string;
  provider: string;
  match(rawDevice: any): boolean;
 
}
