import { createRouter, createWebHistory } from 'vue-router'
import MockDeviceView from '../view/demo/MockDeviceView/index.vue'
import PageView from '../view/demo/PageView/index.vue'
import ProcessView from '../view/demo/ProcessView/index.vue'
import ApplicationView from '../view/demo/ApplicationView/index.vue'
import ApplicationListView from '../view/demo/ApplicationListView/index.vue'
import ResourceView from '../view/demo/ResourceView/index.vue'
import ScenarioView from '../view/demo/ScenarioView/index.vue'
import MainLayout from '../view/main/MainLayout/index.vue'
import HomeView from '../view/main/HomeView/index.vue'
import DomainAndComponentView from '../view/designer/DomainAndComponentView/index.vue'
import { getToken } from '../utils/auth.ts'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '',
      name: '首页',
      component: MainLayout,
      meta: {
        title: '首页',
        keepAlive: true
      },
      children: [
        {
          path: '',
          name: 'main',
          component: HomeView
        },
        {
          path: '/auth',
          name: '权限配置',
          component: null
        },
        {
          path: '/publish-setting',
          name: '应用发布',
          component: null
        },
        {
          path: '/user',
          name: '用户管理',
          component: null
        },
        {
          path: '/template',
          name: '模板库',
          component: null
        },
        {
          path: '/my-workspace',
          name: '我的空间',
          component: DomainAndComponentView
        }
      ]
    },
    {
      path: '/demo',
      name: '咖啡机器人应用demo',
      component: null,
      children: [
        {
          path: '',
          name: '场景列表',
          component: ScenarioView
        },
        {
          path: '/resource',
          name: '资源列表',
          component: ResourceView
        },
        {
          path: '/applicationList',
          name: '应用列表',
          component: ApplicationListView
        },
        {
          path: '/application/detail',
          name: '应用功能列表',
          component: ApplicationView
        },
        {
          path: '/process',
          name: '应用功能流程',
          component: ProcessView
        },
        {
          path: '/page',
          name: '应用功能页面',
          component: PageView
        },
        {
          path: '/mock-device',
          name: '模拟设备页面',
          component: MockDeviceView
        }
      ]
    }
  ]
})

// 登录拦截
router.beforeEach((to, _from, next) => {
  if (to.meta.requiresAuth && !getToken()) {
    // 如果路由需要身份验证并且用户未登录，重定向到登录页
    // next('/login')
    next()
  } else {
    // 否则，继续导航
    next()
  }
})

export default router
