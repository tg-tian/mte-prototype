import { defineStore } from 'pinia'
import { getMockScenes, createMockScene, updateMockScene, deleteMockScene, getScenes, createScene, updateScene, deleteScene, getSceneById } from '@/api/scene'
import { Scene } from '@/types/models'

export const useSceneStore = defineStore('scene', {
    state: () => ({
        scenes: [] as Scene[],
        loading: false,
        currentScene: null as Scene | null,
        // 设置环境变量，默认使用真实API数据
        useMockData: false
    }),

    actions: {
        async fetchScenes(domainId?: number) {
            this.loading = true
            try {
                // 始终使用真实API数据
                const res: any = await getScenes(domainId);
                    
                if (res && res.status === 200) {
                    // 处理真实API返回的数据
                    this.scenes = res.data || [];

                    console.log('Scenes fetched from API:', this.scenes);
                }
            } catch (error) {
                console.error('Failed to fetch scenes:', error)
                // 如果API调用失败，回退到mock数据
                try {
                    console.warn('Falling back to mock data');
                    const mockRes: any = await getMockScenes(domainId);
                    if (mockRes.data && mockRes.data.code === 200) {
                        this.scenes = mockRes.data.data;
                    }
                } catch (mockError) {
                    console.error('Failed to fetch mock scenes:', mockError);
                }
            } finally {
                this.loading = false
            }
        },

        async getSceneById(id: number) {
            try {
                // 始终使用真实API数据
                const res: any = await getSceneById(id);
                    
                if (res && res.status === 200) {
                    this.currentScene = res.data;
                    return res.data;
                }
            } catch (error) {
                console.error('Failed to fetch scene by ID:', error);
                return null;
            }
        },

        async createScene(sceneData: any) {
            try {
                // 始终使用真实API数据
                const res: any = await createScene(sceneData);
                    
                if (res && res.status === 200) {
                    await this.fetchScenes(sceneData.domainId)
                    return res.data
                }
            } catch (error) {
                console.error('Failed to create scene:', error)
                throw error
            }
        },

        async updateScene(id: number, sceneData: any) {
            try {
                // 始终使用真实API数据
                const res: any = await updateScene(id, sceneData);
                
                if (res && res.status === 200) {
                    // 如果当前场景正在被更新，则同步更新它
                    if (this.currentScene && this.currentScene.id === id) {
                        this.currentScene = { ...this.currentScene, ...res.data }
                    }
                    
                    // 刷新场景列表
                    await this.fetchScenes(sceneData.domainId)
                    return res.data
                }
            } catch (error) {
                console.error('Failed to update scene:', error)
                throw error
            }
        },

        async deleteScene(id: number) {
            try {
                // 始终使用真实API数据
                const res: any = await deleteScene(id);
                
                if (res && res.status === 200) {
                    // 如果当前场景被删除，清除它
                    if (this.currentScene && this.currentScene.id === id) {
                        this.currentScene = null
                    }
                    
                    // 从本地数组中移除
                    this.scenes = this.scenes.filter(scene => scene.id !== id)
                    return true
                }
            } catch (error) {
                console.error('Failed to delete scene:', error)
                throw error
            }
        },

        setCurrentScene(scene: Scene) {
            this.currentScene = scene
        }
    },

    persist: true
})
