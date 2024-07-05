import request from '../utils/request'

export const executeProcess = (processId: String, saveConfig: any) =>
    request({
        url: `/execute-process?processId=${processId}`,
        method: 'post',
        data: saveConfig
    })

export const getActionConfig = (actionId: String) =>
    request({
        url: `/action/config?actionId=${actionId}`,
        method: 'get'
    })
