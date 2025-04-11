import { defineStore } from 'pinia'
import { getMockDomains, createMockDomain, updateMockDomain, deleteMockDomain, getDomains, updateDomain, deleteDomain, createDomain } from '@/api/domain'

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

        setCurrentDomain(domain: any) {
            this.currentDomain = domain
        }
    },

    persist: true
})
