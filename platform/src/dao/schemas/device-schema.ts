import mongoose, { Schema, Document } from 'mongoose';

interface IDevice extends Document {
  deviceId: string;
  provider: string;
  category: string;
  deviceName?: string;
  state: {
    reported: Record<string, any>;
    desired: Record<string, any>;
  };
  metadata: {
    lastUpdated: number;
    isOnline: boolean;
    version: number;
  };
}

const DeviceSchema = new Schema<IDevice>({
  deviceId: { type: String, required: true, unique: true },
  provider: { type: String, required: true },
  category: { type: String, required: true },
  deviceName: { type: String },
  state: {
    reported: { type: Schema.Types.Mixed, default: {} },
    desired: { type: Schema.Types.Mixed, default: {} }
  },
  metadata: {
    lastUpdated: { type: Number, default: Date.now },
    isOnline: { type: Boolean, default: false },
    version: { type: Number, default: 1 }
  }
}, { timestamps: true });

export const DeviceModel = mongoose.model<IDevice>('Device', DeviceSchema);
