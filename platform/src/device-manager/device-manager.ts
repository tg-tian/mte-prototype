import { EventEmitter } from 'events';
import { AdapterFactory } from '../adapters/adapter-factory';
import { ShadowManager } from './shadow/shadow-manager';
import { InMemoryShadowManager } from './shadow/in-memory-shadow';
import { DeviceShadow } from '../domain/device-shadow';
import { UnifiedEvent } from '../domain/unified-event';
import { ShadowDAO } from '../dao/shadow-dao';

export class DeviceManager extends EventEmitter {
  private adapterFactory: AdapterFactory;
  private shadowManager: ShadowManager;
  private shadowDAO: ShadowDAO;

  constructor(adapterFactory: AdapterFactory) {
    super();
    this.adapterFactory = adapterFactory;
    this.shadowManager = new InMemoryShadowManager();
    this.shadowDAO = new ShadowDAO();
    for (const adapter of this.adapterFactory.listAdapters()) {
      adapter.setEventHandler((event) => this.handleDeviceEvent(event));
    }
  }

  // 获取所有设备
  async getAllDevices(): Promise<DeviceShadow[]> {
    return await this.shadowManager.getAll();
  }

  async addDevice(device: any): Promise<void> {
    const shadow: DeviceShadow = {
        deviceId: device.deviceId,
        provider: device.provider,
        category: device.category,
        deviceName: device.deviceName,
        deviceModel: device.deviceModel,
        state: { reported: {}, desired: {} },
        metadata: { lastUpdated: Date.now(), isOnline: true, version: 1 }
    };
    await this.shadowManager.addDevice(shadow);
  }

  registerDevice(device: any){
    const adapter = this.adapterFactory.getAdapter(device.provider);
    if (adapter) {
      adapter.registerDevice(device);
    }
  }

  async handleDeviceEvent(event: UnifiedEvent): Promise<void> {
    const deviceId = event.deviceId;
    if (!deviceId) return;
    console.log(`[DeviceManager] Received event type: ${event.type} for ${deviceId}`);

    if (event.type === 'config') {
       console.log(`[DeviceManager] Registering device from config: ${deviceId}`);
       this.emit('device.discovery', event.payload);
    } else if (event.type === 'property') {
      console.log(`[DeviceManager] Updating properties for ${deviceId}`, event.payload);
      await this.shadowManager.updateReported(deviceId, event.payload);
      
      // Sync cache
      const shadow = await this.shadowManager.get(deviceId);
      if (shadow) {
          this.emit('device.updated', shadow);
      }
      
    } else if (event.type === 'event') {
      const evt = event.payload;
      console.log(`[DeviceManager] Event triggered for ${deviceId}: ${evt.event} = ${JSON.stringify(evt.value)}`);
      this.emit('device.event', { deviceId, event: evt });
    }
  }

  async flushNow(): Promise<void> {
    const all = await this.shadowManager.getAll();
    for (const shadow of all) {
      await this.shadowDAO.saveShadow(shadow);
    }
  }
}
