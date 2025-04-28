import templateRequest from '@/utils/templateRequest'
import request from '@/utils/request'


export function getTemplates(data: any, page: number) {
    return templateRequest({
        url: `/templates.json`,
        method: 'get',
        params: {
            page,
            ...data
        }
    })
}

export function getDomainTemplates(domainId: number) {
    return request({
        url: `/templates/domain?domainId=${domainId}`,
        method: 'get'
    })
}

export function bindingTemplates(data: any) {
    return request({
        url: '/templates/binding',
        method: 'post',
        data: data
    })
}

export function unbindingTemplates(domainId: number, id: number) {
    return request({
        url: '/templates/unbinding',
        method: 'post',
        data: {
            domainId: domainId,
            templateId: id
        }
    })
}