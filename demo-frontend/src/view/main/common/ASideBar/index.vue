<template>
  <el-menu :default-active="selectedItem" class="nav-menu" @select="handleMenuSelect">
<!--  管理员面板  -->
    <div style="margin-top: 10px" v-if="roles.includes('admin')">
      <div class="sub-title">系统业务</div>
      <el-sub-menu index="0" class="nav-item">
        <template #title>
          <el-icon><Monitor /></el-icon>
          <div class="menu-item">应用管理</div>
        </template>
        <el-menu-item index="auth" class="sub-menu-item">权限配置</el-menu-item>
        <el-menu-item index="publish-setting" class="sub-menu-item">应用发布</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="userManage" class="nav-item">
        <el-icon><User /></el-icon>
        <div class="menu-item">用户管理</div>
      </el-menu-item>
    </div>
  <!--
  开发人员面板：
  2领域定义员（设置领域——增删改,设定领域组件/模板——UI/流程/设备）
  1场景设置员（设置场景，设定场景资源——设备/外部应用）
  应用开发者（选定场景开发应用，应用增删改）
    -->
    <div style="margin-top: 10px" v-if="roles.includes('developer') || roles.includes('admin')">
      <div class="sub-title">工作台</div>
      <el-menu-item index="template" class="nav-item">
        <el-icon><Compass /></el-icon>
        <div class="menu-item">模板库</div>
      </el-menu-item>
      <el-menu-item index="workspace" class="nav-item">
        <el-icon><DataBoard /></el-icon>
        <div class="menu-item">我的空间</div>
      </el-menu-item>
    </div>
<!--  普通用户面板  -->
    <div style="margin-top: 10px" v-if="roles.includes('user')">
      <el-menu-item index="recommendation" class="nav-item">
        <el-icon><Compass /></el-icon>
        <div class="menu-item">为你推荐</div>
      </el-menu-item>
      <el-menu-item index="application" class="nav-item">
        <el-icon><House /></el-icon>
        <div class="menu-item">我的应用</div>
      </el-menu-item>
      <div>
        <div class="sub-title">设置</div>
        <el-menu-item index="profile" class="nav-item">
          <el-icon><User /></el-icon>
          <div class="menu-item">个人资料</div>
        </el-menu-item>
        <el-menu-item index="setting" class="nav-item">
          <el-icon><Setting /></el-icon>
          <div class="menu-item">账号设置</div>
        </el-menu-item>
      </div>
    </div>
  </el-menu>
</template>

<script setup lang="ts">
import { House, Monitor, User, Compass, DataBoard, Folder, Setting } from '@element-plus/icons-vue'
import router from '../../../../router/index.ts'
import {useUserStore} from "../../../../store/modules/userStore";
import {storeToRefs} from "pinia";

const userStore = useUserStore()
const { roles } = storeToRefs(userStore)

interface State {
  selectedItem: string
  items: any;
}

const state = reactive<State>({
  selectedItem: '',
  items: [
      // 管理员
    {
      index: 'auth',
      name: '权限配置',
      route: '/admin/auth'
    },
    {
      index: 'publish-setting',
      name: '应用发布',
      route: '/admin/publish-setting'
    },
    {
      index: 'userManage',
      name: '用户管理',
      route: '/admin/userManage'
    },
      // 开发人员
    {
      index: 'template',
      name: '模板库',
      route: '/developer/template'
    },
    {
      index: 'workspace',
      name: '我的空间',
      route: '/developer/workspace'
    },
      // 普通用户
    {
      index: 'recommendation',
      name: '为你推荐',
      route: '/user/recommendation'
    },
    {
      index: 'application',
      name: '我的应用',
      route: '/user/my-application'
    },
    {
      index: 'profile',
      name: '个人资料',
      route: '/user/my-profile'
    },
    {
      index: 'setting',
      name: '账号设置',
      route: '/user/my-setting'
    }
  ]
})
const { selectedItem, items } = toRefs(state)

onMounted(async ()=>{
})

watchEffect(() => {
  const path = router.currentRoute.value.path
  const matchingItemData = items.value.find(item => item.route === path);
  if (matchingItemData) {
    selectedItem.value = matchingItemData.index;
  } else {
    selectedItem.value = '';
  }
})

const handleMenuSelect = (key: string, _keyPath: string[]) => {
  selectedItem.value = key
  const selectedItemData = items.value.find(item => item.index === key);
  if (selectedItemData) {
    router.push(selectedItemData.route)
  }
}
</script>
<style scoped>
.nav-menu {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.nav-item {
  width: 100%;
}

.sub-title {
  color: grey;
  margin-left: 10px;
  font-size: 14px;
  margin-bottom: 5px;
}

.menu-item {
  width: 100px;
  text-align: center;
  font-size: 16px;
}

.sub-menu-item {
  text-align: center;
  font-size: 16px;
}
</style>
