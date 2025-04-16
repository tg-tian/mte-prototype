import { defineStore } from 'pinia'
import { getMockDeviceTypes, createMockDeviceType, updateMockDeviceType, deleteMockDeviceType } from '@/api/deviceType'

export const useDeviceTypeStore = defineStore('deviceType', {
    state: () => ({
        deviceTypes: [],
        loading: false,
        currentDeviceType: null
    }),

    actions: {
        async fetchDeviceTypes(domainId?: number) {
            this.loading = true
            try {
                const res: any = await getMockDeviceTypes(domainId)
                if (res.data && res.status === 200) {
                    this.deviceTypes = res.data
                }
            } catch (error) {
                console.error('Failed to fetch deviceTypes:', error)
            } finally {
                this.loading = false
            }
        },

        async createDeviceType(deviceTypeData: any) {
            try {
                const res: any = await createMockDeviceType(deviceTypeData)
                if (res.data && res.status === 200) {
                    await this.fetchDeviceTypes()
                    return res.data
                }
            } catch (error) {
                console.error('Failed to create deviceType:', error)
                throw error
            }
        },

        async updateDeviceType(id: number, deviceTypeData: any) {
            try {
                const res: any = await updateMockDeviceType(id, deviceTypeData)
                if (res.data && res.status === 200) {
                    await this.fetchDeviceTypes()
                    return res.data
                }
            } catch (error) {
                console.error('Failed to update device:', error)
                throw error
            }
        },

        async deleteDeviceType(id: number) {
            try {
                const res: any = await deleteMockDeviceType(id)
                if (res.data && res.status === 200) {
                    await this.fetchDeviceTypes()
                    return true
                }
            } catch (error) {
                console.error('Failed to delete deviceType:', error)
                throw error
            }
        },

        setCurrentDeviceType(deviceType: any) {
            this.currentDeviceType = deviceType
        }
    },

    persist: true
})
