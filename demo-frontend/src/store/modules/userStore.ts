import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    username: '',
    isAuthenticated: false,
    roles: [],
  }),
  getters: {
    getUsername: (state) => state.username,
    getRoles: (state) => state.roles,
    getIsAuthenticated: (state) => state.isAuthenticated
  },
  actions: {
    login(user) {
      console.log(user)
      this.username = user.username
      this.isAuthenticated = true;
      this.roles = user.roles;
    },
    logout() {
      this.username = ''
      this.isAuthenticated = false;
      this.roles = [];
    },
  }
})
