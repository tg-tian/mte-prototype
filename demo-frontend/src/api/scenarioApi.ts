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
