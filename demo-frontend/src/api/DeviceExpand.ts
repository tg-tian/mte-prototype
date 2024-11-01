import request from '../utils/request'

export const initDeviceType = (deviceCode: String, deviceName: String, imageUrl: String) => request({
    url: `/add-device-type/init`,
    method: 'post',
    data: {
        deviceCode,
        deviceName,
        imageUrl
    }
})

export const addDeviceTypeOperation = (deviceType: String, command: any) => request({
    url: `/add-device-type/operation/add`,
    method: 'post',
    data: {
        deviceType,
        command
    }
})

export const addDeviceTypeEvent = (deviceType: String, commandCode: String, deviceEvent: any) => request({
    url: `/add-device-type/event/add`,
    method: 'post',
    data: {
        deviceType,
        commandCode,
        deviceEvent
    }
})

export const addDeviceTypeService = (deviceType: String, brandService: any) => request({
    url: `/add-device-type/service/init`,
    method: 'post',
    data: {
        deviceType,
        brandService
    }
})
export const uploadDeviceType = (data: any) => request({
    url: `/device-type/upload`,
    method: 'post',
    data: data
})

//使用 ? 开始第一个参数，然后后续参数使用 & 连接,url使用反引号``
export const getOperationParam = (deviceName: String , commandCode: String) =>
    request({
        url: `/load-operation-param?deviceName=${deviceName}&commandCode=${commandCode}`,
        method: 'get',
    })

export const getOperationEvent = (deviceName : String , operationCode : String) =>
    request({
        url: `/load-operation-event?deviceName=${deviceName}&operationCode=${operationCode}`,
        method: 'get',
    })

export  const getOperationCommand = (deviceType: String) =>
    request({
        url:`/load-operation-command?deviceType=${deviceType}`,
        methodL:'get',
    })

export  const getService = (deviceName : String , serviceName : String) =>
    request({
        url:`/load-service?deviceName=${deviceName}&serviceName=${serviceName}`,
        methodL:'get',
    })

export const loadDeviceInfo = (deviceTypeCode: String) =>
    request({
        url:`/device-type/info?deviceTypeCode=${deviceTypeCode}`,
        method:'get',
    })

export const updateDevicePublish = (deviceTypeCode: String) =>
    request({
        url:`/device-type/publish?deviceTypeCode=${deviceTypeCode}`,
        method:`put`,
    })
