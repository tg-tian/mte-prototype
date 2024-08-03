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
