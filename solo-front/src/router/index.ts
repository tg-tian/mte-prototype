import { RouteRecordRaw, createRouter, createWebHashHistory } from 'vue-router'
import DomainList from '@/views/meta/DomainList/index.vue'
import Layout from '@/views/components/Layout/index.vue'
const routes: RouteRecordRaw[] = [
    {
        path: '/',
        name: '低代码平台',
        component: Layout,
        meta:{
            keepAlive: true
        },
        children:[
            {
                path: 'meta',
                name: '元工具平台',
                component: null,
                children: [
                    {
                        path: 'domain/list',
                        name: '领域列表',
                        component: DomainList
                    },
                    {
                        path: 'domain/setting',
                        name: '领域定制',
                        component: null
                    }
                ]
            },
            {
                path: 'domain',
                name: '领域平台',
                component: null,
                children: [
                    {
                        path: 'scene/list',
                        name: '场景列表',
                        component: null
                    },
                    {
                        path: 'scene/setting',
                        name: '场景定制',
                        component: null
                    }
                ]
            },
            {
                path: 'scene',
                name: '场景平台',
                component: null,
                children: [
                    {
                        path: 'information',
                        name: '场景信息',
                        component: null
                    },
                    {
                        path: 'device',
                        name: '设备列表',
                        component: null
                    },
                    {
                        path: 'location',
                        name: '场景布局',
                        component: null
                    }
                ]
            }
        ]
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

// 登录拦截
router.beforeEach((_to, _from, next) => {
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