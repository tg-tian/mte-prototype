import {defineStore} from "pinia";

export const useDomainStore = defineStore('domain', {
    state: () => ({
        domainId: '',
        domainName: ''
    }),
    getters: {
        getDomainId: (state) => state.domainId,
        getDomainName: (state) => state.domainName
    },
    actions: {
        loadDomain(domain: any) {
            this.domainId = domain.domainId
            this.domainName = domain.domainName
        }
    },
    persist: true
})
