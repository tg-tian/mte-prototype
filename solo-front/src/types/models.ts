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
    url: string;
    location?: {
        lng: number; // longitude
        lat: number; // latitude
    };
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

//设备类型
export interface DeviceType {
    id: number;
    code: string;
    name: string;
    description: string;
    createTime: string;
    domainIds?: Array<number>,
    model?: Model
}

export interface Model {
    properties: Array<Property>
    services: Array<Service>
    events: Array<Event>
}

export interface Property {
    identify: string;//属性标识符
    name: string;//属性名称
    accessMode?: string;//属性读写类型
    dataType: DataType;//属性数据类型
}

export interface DataType {
    type: string;//类型
    specs: any;//对象，number类型包括min\max\step属性，bool类型包括0\1，string类型包括length
}

export interface Service {
    identify: string;//服务标识符
    name: string;//服务名称
    inputData: Array<Property>;//输入参数
    outputData: Array<Property>;//输出参数
}

export interface Event {
    identify: string;//事件标识符
    name: string;//事件名称
    type: string;//事件类型：信息info、告警warning、故障error
    outputData: Array<Property>;//输出参数，指事件的返回值类型
}

export interface User {
    id: number;
    username: string;
    displayName: string;
    role: string;
    avatar?: string;
    email?: string;
}
