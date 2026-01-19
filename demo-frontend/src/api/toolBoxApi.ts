import request from '../utils/request'

export const loadComponentData = (componentType:String) =>
    request({
        url:`/load-domain-component-data?componentType=${componentType}`,
        method:'get',
    })