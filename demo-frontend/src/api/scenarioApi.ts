import request from '../utils/request'

export const getScenarioJson = () =>
    request({
        url: `/load-scenario-json`,
        method: 'get',
    })

export const getScenarioResource = () =>
    request({
        url: `/load-scenario-resource-json`,
        method: 'get',
    })

/**
 * 从数据库获取领域组件绑定数据
 * @param componentType
 * @param domainName
 */
export const loadScenarioBindingData = (componentType: String ,domainName : String) =>
    request({
        url:`/load-domain-component-binding?componentType=${componentType}&domainName=${domainName}`,
        method:"get",
    })

/**
 * 上传具体设备信息到数据库
 * @param data
 */
export const uploadDeviceData = (data : any) =>
    request({
        url:`/device/data`,
        method:"post",
        data:data,
    })
/**
 * 上传设备注册信息到数据库
 * @param data
 * @param scenarioCode
 * @param deviceName
 */
export  const uploadDeviceRegisterData = (data : any , scenarioCode : String ,deviceName: String) =>
    request({
        url:`/register/device?scenarioCode=${scenarioCode}&deviceName=${deviceName}`,
        method:"post",
        data:data,
    })

export const loadScenarioData = (scenarioCode : String) =>
    request({
        url:`/register/device?scenarioCode=${scenarioCode}`,
        method:"get",
    })