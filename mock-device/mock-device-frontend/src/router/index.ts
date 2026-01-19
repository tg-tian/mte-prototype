import { createRouter, createWebHistory } from 'vue-router'
import MockDevice from '../view/MockDevice.vue'
import {ElMessage} from "element-plus";
const router = createRouter({
  history: createWebHistory(),
  routes: [{
    path: '',
    name: '模拟设备',
    component: null,
    children: [{
      path: '',
      name: '模拟设备',
      component: MockDevice
    }]
  }]
})

// 登录拦截
router.beforeEach((to, _from, next) => {
  next()

  // if (to.meta.requiresAuth && !getToken()) {
  //   // 如果路由需要身份验证并且用户未登录，重定向到登录页
  //   next('/login')
  //   // next()
  // } else {
  //   // 否则，继续导航
  //   next()
  // }
})

export default router
