import {defineStore} from "pinia";

export const useApplicationStore = defineStore('application', {
    state: () => ({
        applicationId: '',
        applicationName: ''
    }),
    getters: {
        getApplicationId: (state) => state.applicationId,
        getApplicationName: (state) => state.applicationName
    },
    actions: {
        loadApplication(application: any) {
            this.applicationId = application.applicationId
            this.applicationName = application.applicationName
        }
    }
})
