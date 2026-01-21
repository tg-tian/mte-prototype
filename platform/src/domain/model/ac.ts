import { BaseDeviceModel } from './base-model';

export const ACModel: BaseDeviceModel = {
  modelName: 'AC',
  provider: 'template',
  category: 'ac',
  properties: {
    tempCurrent: { type: 'number', unit: 'C', min: -20, max: 60 },
    tempTarget: { type: 'number', unit: 'C', min: 16, max: 30 },
    hvacMode: { type: 'enum', enumValues: ['cool', 'heat', 'fan', 'dry', 'auto'] },
    power: { type: 'boolean' }
  },
  actions: {
    setMode: { arguments: { mode: { type: 'string' } } },
    setTemperature: { arguments: { temp: { type: 'number', min: 16, max: 30 } } },
    setPower: { arguments: { on: { type: 'boolean' } } }
  },
  events: {},
  extensions: {}
};
