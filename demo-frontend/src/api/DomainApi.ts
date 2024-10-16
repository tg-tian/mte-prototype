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

/**
 * 从数据库获取领域组件完整数据
 * @param componentType
 */
export const loadDoaminComponentData = (componentType: String) =>
    request({
        url:`/load-domain-component-data?componentType=${componentType}`,
        method:'get',
    })

/**
 * 上传组件绑定信息至数据库
 * @param data
 * @param componentType
 * @param domainName
 */
export const uploadDomainBindingData = (data:any,componentType: String,domainName : String) =>
    request({
        url:`/upload-domain-component-binding?componentType=${componentType}&domainName=${domainName}`,
        method:"post",
        data: data,
    })

/**
 * 从数据库获取组件绑定信息
 * @param componentType
 * @param domainName
 */
export const loadDomainBindingData = (componentType: String ,domainName : String) =>
    request({
        url:`/load-domain-component-binding?componentType=${componentType}&domainName=${domainName}`,
        method:"get",
    })