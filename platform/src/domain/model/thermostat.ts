import { BaseDeviceModel } from './base-model';

export const ThermostatModel: BaseDeviceModel = {
  modelName: 'Thermostat',
  provider: 'template',
  category: 'thermometer',
  properties: {
    tempCurrent: { type: 'number', unit: 'C' },
    tempTarget: { type: 'number', unit: 'C' },
    humidity: { type: 'number', unit: '%' }
  },
  events: {
    overheating: { level: 'warning', fields: { level: { type: 'string' } } }
  },
  extensions: {}
};
