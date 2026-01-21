import { DeviceMapper} from '../device-mapper';
import { BaseDeviceModel, ACModel } from '../../domain/model';

export class MqttACMapper implements DeviceMapper {
  metaModel = ACModel;
  deviceModel = 'HAIER-AC-1001';
  provider = 'mqtt';
  match(rawDevice: any): boolean {
    return rawDevice.provider === this.provider
      && rawDevice.category === this.metaModel.category
      && rawDevice.model === this.deviceModel;
  }

 

  
}
