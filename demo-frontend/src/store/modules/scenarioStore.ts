import {defineStore} from "pinia";

export const useScenarioStore = defineStore('scenario', {
    state: () => ({
        scenarioId: '',
        scenarioName: ''
    }),
    getters: {
        getScenarioId: (state) => state.scenarioId,
        getScenarioName: (state) => state.scenarioName
    },
    actions: {
        loadScenario(scenario: any) {
            this.scenarioId = scenario.scenarioId
            this.scenarioName = scenario.scenarioName
        }
    }
})
