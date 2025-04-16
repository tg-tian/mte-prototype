import request from '@/utils/request'
import { mockDeviceTypes } from './mock'
import { DeviceType } from '@/types/models'

// 实际环境下的API接口
export function getDeviceTypes(domainId?: number) {
    return request({
        url: '/deviceTypes',
        method: 'get',
        params: { domainId }
    })
}

export function getDeviceTypeById(id: number) {
    return request({
        url: `/deviceTypes/${id}`,
        method: 'get'
    })
}

export function createDeviceType(data: any) {
    return request({
        url: '/deviceTypes',
        method: 'post',
        data
    })
}

export function updateDeviceType(id: number, data: any) {
    return request({
        url: `/deviceTypes/${id}`,
        method: 'put',
        data
    })
}

export function deleteDeviceType(id: number) {
    return request({
        url: `/deviceTypes/${id}`,
        method: 'delete'
    })
}

// Mock API functions
export function getMockDeviceTypes(domainId?: number) {
    return new Promise((resolve) => {
        setTimeout(() => {
            let data = [...mockDeviceTypes]
            if (domainId) {
                data = data.filter(d => d.domainIds && d.domainIds.includes(domainId))
            }
            resolve({
                status: 200,
                data
            })
        }, 300)
    })
}

export function getMockDeviceTypeById(id: number) {
    return new Promise((resolve) => {
        setTimeout(() => {
            const deviceType = mockDeviceTypes.find(d => d.id === id)
            resolve({
                status: 200,
                data: deviceType
            })
        }, 300)
    })
}

export function createMockDeviceType(data: any) {
    return new Promise((resolve) => {
        setTimeout(() => {
            const newDeviceType: DeviceType = {
                id: mockDeviceTypes.length + 1,
                ...data,
                createTime: new Date().toISOString().split('.')[0].replace('T', ' ')
            }
            mockDeviceTypes.push(newDeviceType)
            resolve({
                status: 200,
                data: newDeviceType
            })
        }, 300)
    })
}

export function updateMockDeviceType(id: number, data: any) {
    return new Promise((resolve) => {
        setTimeout(() => {
            const index = mockDeviceTypes.findIndex(d => d.id === id)
            if (index !== -1) {
                mockDeviceTypes[index] = {
                    ...mockDeviceTypes[index],
                    ...data
                }
                resolve({
                    status: 200,
                    data: mockDeviceTypes[index]
                })
            } else {
                resolve({
                    status: 404,
                    data: 'Device not found'
                })
            }
        }, 300)
    })
}

export function deleteMockDeviceType(id: number) {
    return new Promise((resolve) => {
        setTimeout(() => {
            const index = mockDeviceTypes.findIndex(d => d.id === id)
            if (index !== -1) {
                mockDeviceTypes.splice(index, 1)
                resolve({
                    status: 200,
                    data: null
                })
            } else {
                resolve({
                    status: 404,
                    data: 'Device not found'
                })
            }
        }, 300)
    })
}
