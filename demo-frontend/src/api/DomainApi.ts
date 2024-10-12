import request from '../utils/request'

export const getDomainJson = () =>
    request({
        url: `/load-domain-json`,
        method: 'get',
    })

export const getDomainComponent = (componentType: String) =>
    request({
        url: `/load-domain-component-json?componentType=${componentType}`,
        method: 'get',
    })

export const getDoaminComponentData = (componentType: String) =>
    request({
        url:`/load-domain-component-data?componentType=${componentType}`,
        method:'get',
    })

export const uploadDomainBindingData = (data:any,componentType: String,domainName : String) =>
    request({
        url:`/upload-domain-component-binding?componentType=${componentType}&domainName=${domainName}`,
        method:"post",
        data: data,
    })