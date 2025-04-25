import { reactive } from 'vue'
import { Domain, Scene, Device, DeviceType, Template } from '@/types/models'

// Mock domain data
export const mockDomains = reactive<Domain[]>([
    {
        id: 1,
        name: '智能制造领域',
        description: '面向工业4.0的智能制造解决方案',
        createTime: '2023-10-15',
        updateTime: '2023-11-20',
        sceneCount: 5,
        status: 'active'
    },
    {
        id: 2,
        name: '智慧城市领域',
        description: '城市数字孪生与智能监控管理平台',
        createTime: '2023-09-22',
        updateTime: '2023-12-05',
        sceneCount: 8,
        status: 'active'
    },
    {
        id: 3,
        name: '医疗健康领域',
        description: '医疗服务与健康管理数字化平台',
        createTime: '2023-07-30',
        updateTime: '2023-10-12',
        sceneCount: 3,
        status: 'active'
    },
    {
        id: 4,
        name: '教育科研领域',
        description: '教育资源管理与科研协作平台',
        createTime: '2023-11-05',
        updateTime: '2023-12-18',
        sceneCount: 2,
        status: 'inactive'
    }
])

// Mock scenes data
export const mockScenes = reactive<Scene[]>([
    {
        id: 1,
        domainId: 1,
        name: '车间生产线监控',
        description: '实时监控生产线设备运行状态与产能',
        createTime: '2023-10-20',
        updateTime: '2023-12-01',
        deviceCount: 12,
        status: 'active',
        url: '',
        location: {
            lng: 121.4737, // Shanghai coordinates
            lat: 31.2304
        }
    },
    {
        id: 2,
        domainId: 1,
        name: '质量检测分析',
        description: '产品质量自动检测与数据分析',
        createTime: '2023-10-25',
        updateTime: '2023-11-30',
        deviceCount: 8,
        status: 'active',
        url: '',
        location: {
            lng: 121.5012,
            lat: 31.2352
        }
    },
    {
        id: 3,
        domainId: 2,
        name: '交通流量监控',
        description: '城市主要道路交通流量实时监控',
        createTime: '2023-09-25',
        updateTime: '2023-12-10',
        deviceCount: 24,
        status: 'active',
        url: '',
        location: {
            lng: 116.4074, // Beijing coordinates
            lat: 39.9042
        }
    }
])

// Mock devices data
export const mockDevices = reactive<Device[]>([
    {
        id: 1,
        sceneId: 1,
        deviceCode: 'TemperatureA1',
        deviceName: '温度传感器-A1',
        deviceTypeId: 1,
        status: 1,
        lastOnlineTime: '2023-12-20 14:30:45',
        createTime: '',
        updateTime: '',
        protocolType: 'MQTT',
        protocolConfig: {
            type: "aliyun",
            configs: {}
        }
    },
    {
        id: 2,
        sceneId: 1,
        deviceCode: 'PressureP1',
        deviceName: '压力监测器-P2',
        deviceTypeId: 1,
        status: 1,
        lastOnlineTime: '2023-12-20 14:35:22',
        createTime: '',
        updateTime: '',
        protocolType: 'MQTT',
        protocolConfig: {
            type: "none",
            configs: {}
        }
    }
])

// Mock deviceTypeData
export const mockDeviceTypes = reactive<DeviceType[]>([
    {
        id: 1,
        code: "CoffeeMaker",
        name: "智能咖啡机",
        description: "智能咖啡机能够自动制作多种咖啡",
        createTime: "2023-12-20 14:30:45",
        updateTime: null,
        domainIds: [1],
        model: {
            properties: [
                {
                    identify: "water",
                    name: "水量",
                    accessMode: "rw",
                    dataType: {
                        type: "float",
                        specs: {
                            "min": 0,
                            "max": 100
                        }
                    }
                }
            ],
            services: [
                {
                    identify: "makeCoffee",
                    name: "制作咖啡",
                    inputData: [
                        {
                            identify: "coffeeType",
                            name: "咖啡类型",
                            dataType: {
                                type: "string",
                                specs: {
                                    "length": 200
                                }
                            }
                        }
                    ],
                    outputData: []
                }
            ],
            events: [
                {
                    identify: "makeCoffeeReply",
                    name: "咖啡制作完成",
                    type: "info",
                    outputData: [
                        {
                            identify: "message",
                            name: "消息",
                            dataType: {
                                type: "string",
                                specs: {
                                    "length": 200
                                }
                            }
                        }
                    ]
                }
            ]
        }
    },
    {
        id: 2,
        code: "SmokeDetector",
        name: "烟感器",
        description: "能够检测烟雾浓度",
        createTime: "2024-12-20 14:30:45",
        updateTime: null,
        model: {
            properties: [
                {
                    identify: "deviceStatus",
                    name: "状态",
                    accessMode: "rw",
                    dataType: {
                        type: "string",
                        specs: {
                            "length": 200
                        }
                    }
                }
            ],
            services: [],
            events: []
        }
    }
])

// Mock users data
export const mockUsers = reactive([
    {
        id: 1,
        username: 'admin',
        password: '123456', // Note: In real app, passwords should never be stored in plaintext
        displayName: '管理员',
        role: 'admin',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    {
        id: 2,
        username: 'user',
        password: '123456',
        displayName: '普通用户',
        role: 'user',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    }
])

// Mock templates
export const mockTemplates = reactive<Template[]>([
    {
        name: '模板1',
        identifier: 'Personal-UIX',
        appKind: '',
        creator: 'xxx',
        price: '',
        description: '这是一个页面模板',
        tags: ['页面'],
        uuid: '1234567'
    },
    {
        name: '模板2',
        identifier: 'Template2',
        appKind: '',
        creator: 'xxx',
        price: '',
        description: '这是一个组件模板',
        tags: ['组件'],
        uuid: '54625552'
    }
])
