import { defineStore } from 'pinia'
import { getMockDomains, createMockDomain, updateMockDomain, deleteMockDomain } from '@/api/domain'

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
                const res: any = await getMockDomains()
                if (res.data && res.data.code === 200) {
                    this.domains = res.data.data
                }
            } catch (error) {
                console.error('Failed to fetch domains:', error)
            } finally {
                this.loading = false
            }
        },

        async createDomain(domainData: any) {
            try {
                const res: any = await createMockDomain(domainData)
                if (res.data && res.data.code === 200) {
                    await this.fetchDomains()
                    return res.data.data
                }
            } catch (error) {
                console.error('Failed to create domain:', error)
                throw error
            }
        },

        async updateDomain(id: number, domainData: any) {
            try {
                const res: any = await updateMockDomain(id, domainData)
                if (res.data && res.data.code === 200) {
                    await this.fetchDomains()
                    return res.data.data
                }
            } catch (error) {
                console.error('Failed to update domain:', error)
                throw error
            }
        },

        async deleteDomain(id: number) {
            try {
                const res: any = await deleteMockDomain(id)
                if (res.data && res.data.code === 200) {
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
