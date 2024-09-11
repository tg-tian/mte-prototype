import request from '../utils/request'


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

export  const getOperationCommand = (devicePath : String) =>
    request({
        url:`/load-operation-command?devicePath=${devicePath}`,
        methodL:'get',
    })

export  const getService = (deviceName : String , serviceName : String) =>
    request({
        url:`/load-service?deviceName=${deviceName}&serviceName=${serviceName}`,
        methodL:'get',
    })