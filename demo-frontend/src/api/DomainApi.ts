import request from '../utils/request'

export const getDomainJson = () =>
    request({
        url: `/load-domain-json`,
        method: 'get',
    })