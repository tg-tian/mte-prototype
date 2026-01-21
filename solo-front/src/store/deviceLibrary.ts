import { defineStore } from 'pinia'
import { 
    getDeviceLibraryPage, 
    getDeviceLibraryList, 
    saveDeviceLibrary, 
    updateDeviceLibrary, 
    deleteDeviceLibrary 
} from '@/api/deviceLibrary'
import { DeviceLibrary } from '@/types/models'

export const useDeviceLibraryStore = defineStore('deviceLibrary', {
    state: () => ({
        deviceLibraryPage: {
            records: [] as DeviceLibrary[],
            total: 0,
            size: 10,
            current: 1
        },
        loading: false,
        currentDeviceLibrary: null as DeviceLibrary | null
    }),

    actions: {
        async fetchDeviceLibraryPage(params: any) {
            this.loading = true
            try {
                const res: any = await getDeviceLibraryPage(params)
                if (res.status === 200) {
                    this.deviceLibraryPage = res.data
                }
            } catch (error) {
                console.error('Failed to fetch device library page:', error)
            } finally {
                this.loading = false
            }
        },

        async saveDevice(data: DeviceLibrary) {
            try {
                const res: any = await saveDeviceLibrary(data)
                return res.status === 201 || res.status === 200
            } catch (error) {
                console.error('Failed to save device library:', error)
                throw error
            }
        },

        async updateDevice(data: DeviceLibrary) {
            try {
                const res: any = await updateDeviceLibrary(data)
                return res.status === 200
            } catch (error) {
                console.error('Failed to update device library:', error)
                throw error
            }
        },

        async deleteDevice(id: number) {
            try {
                const res: any = await deleteDeviceLibrary(id)
                return res.status === 200
            } catch (error) {
                console.error('Failed to delete device library:', error)
                throw error
            }
        },

        setCurrentDeviceLibrary(data: DeviceLibrary | null) {
            this.currentDeviceLibrary = data
        }
    },

    persist: true
})
