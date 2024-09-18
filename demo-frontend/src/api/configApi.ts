import request from "@/utils/request";

export const getDeveloperConfig = ()=>
    request({
        url: `/setting/developer`,
        method: 'get'
    })

export const editDeveloperConfig = (config: any) =>
    request({
        url: `/setting/developer/edit`,
        method: 'post',
        data: config
    })
