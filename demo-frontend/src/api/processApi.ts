import request from '../utils/request'

export const executeProcess = (processId: String) =>
    request({
        url: `/execute-process?processId=${processId}`,
        method: 'post'
    })

export const getActionConfig = (actionId: String) =>
    request({
        url: `/action/config?actionId=${actionId}`,
        method: 'get'
    })
