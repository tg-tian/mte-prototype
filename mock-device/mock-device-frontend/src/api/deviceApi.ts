import request from '../utils/request'

export const getMockDeviceInformationA = () =>
    request({
        url: '/mock-device-information/a',
        method: 'get'
    })

export const getMockDeviceInformationB = () =>
    request({
        url: '/mock-device-information/b',
        method: 'get'
    })
