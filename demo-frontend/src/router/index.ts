import { createRouter, createWebHistory } from 'vue-router'
import MockDeviceView from '../view/demo/MockDeviceView/index.vue'
import PageDemoView from '../view/demo/PageView/index.vue'
import ProcessDemoView from '../view/demo/ProcessView/index.vue'
import ApplicationDemoView from '../view/demo/ApplicationView/index.vue'
import ApplicationListDemoView from '../view/demo/ApplicationListView/index.vue'
import ResourceDemoView from '../view/demo/ResourceView/index.vue'
import ScenarioDemoView from '../view/demo/ScenarioView/index.vue'
import MainLayout from '../view/main/common/MainLayout/index.vue'
import HomeView from '../view/main/common/HomeView/index.vue'
import WorkspaceView from '../view/main/developer/Workspace/index.vue'
import RecommendationView from '../view/main/user/Recommendation/index.vue'
import AddDeviceComponent from '../view/main/developer/designer/component/AddDeviceType.vue'
import DeviceComponentDetail from '../view/main/developer/designer/component/DeviceComponentDetail.vue'
import ScenarioDetail from '../view/main/developer/designer/scenario/ScenarioDetail.vue'
import DomainDetail from "@/view/main/developer/designer/domain/DomainDetail.vue"
import ApplicationDetail from '../view/main/developer/designer/application/ApplicationDetail.vue'
import DesignIndex from '../view/main/developer/designer/application/detailComponents/DesignIndex.vue'
import DesignProcess from '../view/main/developer/designer/application/detailComponents/DesignProcess.vue'
import DesignPage from '../view/main/developer/designer/application/detailComponents/DesignPage.vue'
import ProcessEdit from '../view/main/developer/designer/application/process/ProcessEdit.vue'
import PageEdit from '../view/main/developer/designer/application/page/PageEdit.vue'
import DeveloperConfig from '../view/main/developer/DeveloperConfig/index.vue'
import { getToken } from '../utils/auth.ts'
import { useUserStore } from "../store/modules/userStore";
import {ElMessage} from "element-plus";
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
          path: 'admin',
          name: '管理员页面',
          component: null,
          meta: {
            requiresAuth: true,
            roles: ['admin']
          },
          children: [
            {
              path: 'auth',
              name: '权限配置',
              component: null
            },
            {
              path: 'publish-setting',
              name: '应用发布',
              component: null
            },
            {
              path: 'userManage',
              name: '用户管理',
              component: null
            },
          ]
        },
        {
          path: 'developer',
          name: '开发人员页面',
          component: null,
          meta: {
            requiresAuth: true,
            roles: ['admin','developer']
          },
          children: [
            {
              path: 'template',
              name: '模板库',
              component: null
            },
            {
              path: 'workspace',
              name: '我的空间',
              component: WorkspaceView
            },
            {
              path: 'device-component/detail',
              name: '设备模板详情',
              component: DeviceComponentDetail
            },
            {
              path: 'device-component/add',
              name: '新增设备模板',
              component: AddDeviceComponent
            },
            {
              path: 'scenario/detail',
              name: '场景详情',
              component: ScenarioDetail
            },
            {
              path:'domain/detail',
              name:'领域详情',
              component: DomainDetail
            },
            {
              path: 'setting',
              name: '开发者设置',
              component: DeveloperConfig
            }
          ]
        },
        {
          path: 'user',
          name: '普通用户页面',
          component: null,
          meta: {
            requiresAuth: true,
            roles: ['user']
          },
          children: [
            {
              path: 'recommendation',
              name: '为你推荐',
              component: RecommendationView
            },
            {
              path: 'my-application',
              name: '我的应用',
              component: null
            },
            {
              path: 'my-profile',
              name: '个人资料',
              component: null
            },
            {
              path: 'my-setting',
              name: '账号设置',
              component: null
            }
          ]
        }
      ]
    },
    {
      path: '/design',
      name: '设计页面',
      component: null,
      children: [
        {
          path: '',
          name: '设计目录',
          component: ApplicationDetail,
          children: [
            {
              path: 'index',
              name: '设计首页',
              component: DesignIndex
            },
            {
              path: 'process',
              name: '流程列表',
              component: DesignProcess
            },
            {
              path: 'page',
              name: '页面列表',
              component: DesignPage
            },
          ]
        },
        {
          path: 'edit',
          name: '编辑界面',
          component: null,
          children: [
            {
              path: 'process',
              name: '流程编辑页面',
              component: ProcessEdit
            },
            {
              path: 'page',
              name: '视图编辑页面',
              component: PageEdit
            }
          ]
        }
      ]
    },
    {
      path: '/demo',
      name: '应用demo',
      component: null,
      children: [
        {
          path: '',
          name: '智慧楼宇',
          children: [
            {
              path: '',
              name: '场景列表',
              component: ScenarioDemoView
            },
            {
              path: 'resource',
              name: '资源列表',
              component: ResourceDemoView
            },
            {
              path: 'applicationList',
              name: '应用列表',
              component: ApplicationListDemoView
            },
            {
              path: 'application/detail',
              name: '应用功能列表',
              component: ApplicationDemoView
            },
            {
              path: 'process',
              name: '应用功能流程',
              component: ProcessDemoView
            },
            {
              path: 'page',
              name: '应用功能页面',
              component: PageDemoView
            },
            {
              path: 'mock-device',
              name: '模拟设备页面',
              component: MockDeviceView
            }
          ]
        }
      ]
    }
  ]
})

// 登录拦截
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore();
  const { isAuthenticated, roles } = userStore;

  if (to.meta.requiresAuth){
    if (!isAuthenticated) {
      ElMessage.warning('请先登录')
      next('/');
    } else if (to.meta.roles && !to.meta.roles.some(role => roles.includes(role))) {
      ElMessage.warning('权限不足')
      next('/');
    } else {
      next();
    }
  } else {
    next()
  }

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
