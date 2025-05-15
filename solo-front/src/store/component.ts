import { defineStore } from 'pinia'
import { Component } from '@/types/models'
import { getComponents, getComponentById, createComponent, updateComponent, deleteComponent, mockComponents } from '@/api/component'

export const useComponentStore = defineStore('component', {
  state: () => ({
    components: [] as Component[],
    currentComponent: null as Component | null,
    loading: false
  }),
  
  actions: {
    async fetchComponents() {
      this.loading = true
      try {
        // For production:
        // const response = await getComponents()
        // if (response.code === 200) {
        //   this.components = response.data
        // }
        
        // For development with mock data:
        this.components = mockComponents
        return this.components
      } catch (error) {
        console.error('Failed to fetch components:', error)
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async fetchComponentById(id: number) {
      this.loading = true
      try {
        // For production:
        // const response = await getComponentById(id)
        // if (response.code === 200) {
        //   this.currentComponent = response.data
        // }
        
        // For development with mock data:
        const component = mockComponents.find(c => c.id === id)
        if (component) {
          this.currentComponent = component
        }
        return this.currentComponent
      } catch (error) {
        console.error(`Failed to fetch component with id ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async createComponent(componentData: Component) {
      this.loading = true
      try {
        // For production:
        // const response = await createComponent(componentData)
        // if (response.code === 200) {
        //   this.fetchComponents() // Refresh the list
        //   return response.data
        // }
        
        // For development with mock data:
        const newComponent = { ...componentData, id: mockComponents.length + 1 }
        mockComponents.push(newComponent)
        await this.fetchComponents()
        return newComponent
      } catch (error) {
        console.error('Failed to create component:', error)
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async updateComponent(id: number, componentData: Component) {
      this.loading = true
      try {
        // For production:
        // const response = await updateComponent(id, componentData)
        // if (response.code === 200) {
        //   this.fetchComponents() // Refresh the list
        //   return response.data
        // }
        
        // For development with mock data:
        const index = mockComponents.findIndex(c => c.id === id)
        if (index !== -1) {
          mockComponents[index] = { ...componentData, id }
          await this.fetchComponents()
          return mockComponents[index]
        }
        throw new Error('Component not found')
      } catch (error) {
        console.error(`Failed to update component with id ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async deleteComponent(id: number) {
      this.loading = true
      try {
        // For production:
        // const response = await deleteComponent(id)
        // if (response.code === 200) {
        //   this.fetchComponents() // Refresh the list
        //   return true
        // }
        
        // For development with mock data:
        const index = mockComponents.findIndex(c => c.id === id)
        if (index !== -1) {
          mockComponents.splice(index, 1)
          await this.fetchComponents()
          return true
        }
        throw new Error('Component not found')
      } catch (error) {
        console.error(`Failed to delete component with id ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    },
    
    setCurrentComponent(component: Component) {
      this.currentComponent = component
    }
  },
  
  persist: true
})
