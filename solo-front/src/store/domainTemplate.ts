import { mockTemplates } from '@/api/mock'
import { defineStore } from 'pinia'

export const useDomainTemplateStore = defineStore('domainTemplate', {
    state: () => ({
        allTemplates: [],
        templates: [],
        loading: false,
        currentDomainId: null
    }),

    actions: {
        async fetchTemplates(domainId: number) {
            // 获取领域绑定的模板列表
        },

        async fetchAllTemplates() {
            // 获取模板库的模板列表
            this.allTemplates = mockTemplates
        },

        async bindingTemplates(domainId: number, templateId: string[]){
            // 绑定模板
        },

        async unbindingTemplates(domainId: number, templateId: string){
            // 取消绑定模板
        },

        setCurrentDomain(domainId: number) {
            this.currentDomainId = domainId
        }
    },

    persist: true
})