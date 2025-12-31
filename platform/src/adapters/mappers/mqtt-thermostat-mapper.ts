import { DeviceMapper} from '../device-mapper';
import { BaseDeviceModel, ThermostatModel } from '../../domain/model';

export class MqttThermostatMapper implements DeviceMapper {
  metaModel = ThermostatModel;
  deviceModel = 'MI-THERMO-S01';
  provider = 'mqtt';
  match(rawDevice: any): boolean {
    return rawDevice.provider === this.provider
      && rawDevice.category === this.metaModel.category
      && rawDevice.model === this.deviceModel;
  }
  
}
