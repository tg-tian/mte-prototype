export interface Domain {
    id: number;
    name: string;
    description: string;
    createTime: string;
    updateTime: string;
    sceneCount: number;
    status: 'active' | 'inactive';
}

export interface Scene {
    id: number;
    domainId: number;
    name: string;
    description: string;
    createTime: string;
    updateTime: string;
    deviceCount: number;
    status: 'active' | 'inactive';
}

export interface DeviceLocation {
    x: number;
    y: number;
}

export interface Device {
    id: number;
    sceneId: number;
    name: string;
    type: string;
    status: 'online' | 'offline';
    lastUpdated: string;
    location: DeviceLocation;
    properties?: Record<string, any>;
}

export interface User {
    id: number;
    username: string;
    displayName: string;
    role: string;
    avatar?: string;
    email?: string;
}
