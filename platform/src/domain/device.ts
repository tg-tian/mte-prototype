export interface Device {
  deviceId: string;
  provider: string;
  category: string;
  deviceName?: string;
  state: {
    reported: Record<string, any>;
    desired?: Record<string, any>;
  };
  metadata: {
    lastUpdated: number;
    isOnline: boolean;
    version: number;
  };
}
