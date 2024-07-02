import request from '../utils/request'

export const getMockDeviceInformation = () =>
  request({
    url: '/mock-device-information',
    method: 'get'
  })
