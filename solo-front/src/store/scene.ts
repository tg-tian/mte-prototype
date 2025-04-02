import { defineStore } from 'pinia'
import { getMockScenes, createMockScene, updateMockScene, deleteMockScene } from '@/api/scene'

export const useSceneStore = defineStore('scene', {
    state: () => ({
        scenes: [],
        loading: false,
        currentScene: null
    }),

    actions: {
        async fetchScenes(domainId?: number) {
            this.loading = true
            try {
                const res: any = await getMockScenes(domainId)
                if (res.data && res.data.code === 200) {
                    this.scenes = res.data.data
                }
            } catch (error) {
                console.error('Failed to fetch scenes:', error)
            } finally {
                this.loading = false
            }
        },

        async createScene(sceneData: any) {
            try {
                const res: any = await createMockScene(sceneData)
                if (res.data && res.data.code === 200) {
                    await this.fetchScenes(sceneData.domainId)
                    return res.data.data
                }
            } catch (error) {
                console.error('Failed to create scene:', error)
                throw error
            }
        },

        async updateScene(id: number, sceneData: any) {
            try {
                const res: any = await updateMockScene(id, sceneData)
                if (res.data && res.data.code === 200) {
                    await this.fetchScenes()
                    return res.data.data
                }
            } catch (error) {
                console.error('Failed to update scene:', error)
                throw error
            }
        },

        async deleteScene(id: number) {
            try {
                const res: any = await deleteMockScene(id)
                if (res.data && res.data.code === 200) {
                    await this.fetchScenes()
                    return true
                }
            } catch (error) {
                console.error('Failed to delete scene:', error)
                throw error
            }
        },

        setCurrentScene(scene: any) {
            this.currentScene = scene
        }
    },

    persist: true
})
