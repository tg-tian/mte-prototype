import { defineStore } from 'pinia'
import { getMockDomains, createMockDomain, updateMockDomain, deleteMockDomain, getDomains, updateDomain, deleteDomain, createDomain, publishDomain ,convertDomain,importDomain } from '@/api/domain'

export const useDomainStore = defineStore('domain', {
    state: () => ({
        domains: [],
        loading: false,
        currentDomain: null
    }),

    actions: {
        async fetchDomains() {
            this.loading = true
            try {
                const res: any = await getDomains()
                if (res.data && res.status === 200) {
                    this.domains = res.data
                }
            } catch (error) {
                console.error('Failed to fetch domains:', error)
            } finally {
                this.loading = false
            }
        },

        async createDomain(domainData: any) {
            try {
                const res: any = await createDomain(domainData)
                if (res.data && res.status === 200) {
                    await this.fetchDomains()
                    return res.data
                }
            } catch (error) {
                console.error('Failed to create domain:', error)
                throw error
            }
        },

        async updateDomain(id: number, domainData: any) {
            try {
                const res: any = await updateDomain(id, domainData)
                if (res.data && res.status === 200) {
                    await this.fetchDomains()
                    return res.data
                }
            } catch (error) {
                console.error('Failed to update domain:', error)
                throw error
            }
        },

        async deleteDomain(id: number) {
            try {
                const res: any = await deleteDomain(id)
                if (res.data && res.status === 200) {
                    await this.fetchDomains()
                    return true
                }
            } catch (error) {
                console.error('Failed to delete domain:', error)
                throw error
            }
        },

        async publishDomain(domainId: number, url: string, status: string) {
            try {
                let data = {
                    domainId: domainId,
                    status: status??'1',
                    url: url
                }
                const res: any = await publishDomain(data);
                
                if (res && res.status === 200) {
                    // 如果当前场景正在被更新，则同步更新它
                    if (this.currentDomain && this.currentDomain.id === domainId) {
                        this.currentDomain = { ...this.currentDomain, ...res.data }
                    }
                    
                    // 刷新场景列表
                    await this.fetchDomains()
                    return res.data
                }
            } catch (error) {
                console.error('Failed to publish domain:', error)
                throw error
            }
        },

        setCurrentDomain(domain: any) {
            this.currentDomain = domain
        },

        //todo:待重构为单独使用一个存储空间存储领域模版内容
        //将领域本身保存为模版
        async convertDomain(domainData:any , templates:any , deviceTypes:any,components:any){
            try {
                let data = {
                    domainData: domainData,
                    templates: templates,
                    deviceTypes: deviceTypes,
                    components: components
                }
                const res: any = await convertDomain(data);
                if (res && res.status === 200) {
                    return true
                }
            }catch (error){
                console.error('Failed to save template:', error)
                throw error
            }
        },

        //导入领域模版列表
        async importDomain(){
            try {
                const res: any = await importDomain();
                if (res.data && res.status === 200) {
                    return res.data;
                }
            }catch (error){
                console.error('Failed to import template:', error)
                throw error
            }
        },
    },

    persist: true
})
