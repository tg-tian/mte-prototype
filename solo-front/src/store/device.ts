import { defineStore } from 'pinia'
import {
    createMockDevice,
    updateMockDevice,
    deleteMockDevice,
    getDeviceById,
    getDevices,
    createDevice,
    updateDevice,
    deleteDevice
} from '@/api/device'

export const useDeviceStore = defineStore('device', {
    state: () => ({
        devices: [],
        loading: false,
        currentDevice: null
    }),

    actions: {
        async fetchDevices(sceneId?: number) {
            if(!sceneId){
                this.devices = []
                return
            }
            this.loading = true
            try {
                const res: any = await getDevices(sceneId)
                if (res.data && res.status === 200) {
                    this.devices = res.data
                }
            } catch (error) {
                console.error('Failed to fetch devices:', error)
            } finally {
                this.loading = false
            }
        },

        async createDevice(deviceData: any) {
            try {
                const res: any = await createDevice(deviceData)
                if (res.data && res.data.code === 200) {
                    await this.fetchDevices(deviceData.sceneId)
                    return res.data.data
                }
            } catch (error) {
                console.error('Failed to create device:', error)
                throw error
            }
        },

        async updateDevice(id: number, deviceData: any) {
            try {
                const res: any = await updateDevice(id, deviceData)
                if (res.data && res.data.code === 200) {
                    await this.fetchDevices()
                    return res.data.data
                }
            } catch (error) {
                console.error('Failed to update device:', error)
                throw error
            }
        },

        async deleteDevice(id: number) {
            try {
                const res: any = await deleteDevice(id)
                if (res.data && res.data.code === 200) {
                    await this.fetchDevices()
                    return true
                }
            } catch (error) {
                console.error('Failed to delete device:', error)
                throw error
            }
        },

        setCurrentDevice(device: any) {
            this.currentDevice = device
        }
    },

    persist: true
})
