import { defineStore } from 'pinia'

//定义并且导出容器
//参数1 ： 容器ID ，必须唯一， 将来Pinia 会把所有的容器挂载在根容器
//参数2 ： 选项对象
export const useUserStore = defineStore('user', {

/**组件的data， 用于存储全局状态, 并实现初始化
 * 1.必须是函数，这样是为了在服务端渲染的时候避免交叉请求导致的数据状态污染
 * 2.必须是箭头函数，这是为了更好的TS类型推到
 * 返回值：一个函数，调用得到容器
 */


  state: () => ({
    username: '',
    isAuthenticated: false,
    roles: [],
  }),

  //类似于组件的computed ，用来封装计算属性，有缓存的功能
  //函数接收一个可选参数state 状态对象
  //如果在 getters中使用了this ,则必须手动指定返回值的类型，后者类型推推导不出来
  getters: {
    getUsername: (state) => state.username,
    getRoles: (state) => state.roles,
    getIsAuthenticated: (state) => state.isAuthenticated
  },

  //类似于组件的 methods，封装业务逻辑， 修改 state
  //不能使用箭头函数定义action，因为箭头函数绑定外部 this
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
