<template>
    <div class="nav">
      <div class="logo">LOGO</div>
      <el-menu
      :default-active="selectedItem"
      class="nav-menu"
      @select="handleMenuSelect"
      :style="{
        '--el-menu-bg-color': '#0464e4',
        '--el-menu-text-color': 'rgba(255, 255, 255, 0.65)',
        '--el-menu-active-color': '#0464e4'
      }"
      >
      <!-- 元工具平台 -->
      <div style="margin-top: 10px" v-if="!routerPath.startsWith('/domain') && !routerPath.startsWith('/scene')">
        <el-sub-menu index="0" class="nav-item">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <div class="menu-item">元建模</div>
          </template>
          <el-menu-item index="meta-devicetype-list" class="sub-menu-item">设备类型列表</el-menu-item>
          <el-menu-item index="meta-devicetype-setting" class="sub-menu-item">设备类型定制</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="1" class="nav-item">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <div class="menu-item">定制领域</div>
          </template>
          <el-menu-item index="meta-domain-list" class="sub-menu-item">领域平台列表</el-menu-item>
          <el-menu-item index="meta-domain-setting" class="sub-menu-item">领域平台定制</el-menu-item>
        </el-sub-menu>
      </div>
      <!-- 领域平台 -->
      <div style="margin-top: 10px" v-if="routerPath.startsWith('/domain')">
        <el-sub-menu index="0" class="nav-item">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <div class="menu-item">场景管理</div>
          </template>
          <el-menu-item index="domain-scene-list" class="sub-menu-item">场景平台列表</el-menu-item>
          <el-menu-item index="domain-scene-setting" class="sub-menu-item">场景平台定制</el-menu-item>
        </el-sub-menu>
      </div>
      <!-- 场景平台 -->
      <div style="margin-top: 10px" v-if="routerPath.startsWith('/scene')">
        <el-sub-menu index="0" class="nav-item">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <div class="menu-item">场景信息</div>
          </template>
          <el-menu-item index="scene-information" class="sub-menu-item">场景信息</el-menu-item>
          <el-menu-item index="scene-device" class="sub-menu-item">设备列表</el-menu-item>
          <el-menu-item index="scene-location" class="sub-menu-item">场景布局</el-menu-item>
        </el-sub-menu>
      </div>
      </el-menu>
    </div>
</template>
  
  <script setup lang="ts">
  import { Monitor } from '@element-plus/icons-vue'
  import router from '@/router/index.ts'
  
  interface State {
    selectedItem: string
    items: any;
    routerPath: string
  }
  
  const state = reactive<State>({
    selectedItem: '',
    items: [
      // 元工具平台
      {
        index: 'meta-devicetype-list',
        name: '设备类型列表',
        route: '/meta/devicetype/list'
      },
      {
        index: 'meta-devicetype-setting',
        name: '设备类型定制',
        route: '/meta/devicetype/setting'
      },
      {
        index: 'meta-domain-list',
        name: '领域列表',
        route: '/meta/domain/list'
      },
      {
        index: 'meta-domain-setting',
        name: '领域定制',
        route: '/meta/domain/setting'
      },
      // 领域平台
      {
        index: 'domain-scene-list',
        name: '场景列表',
        route: '/domain/scene/list'
      },
      {
        index: 'domain-scene-setting',
        name: '场景定制',
        route: '/domain/scene/setting'
      },
      // 场景平台
      {
        index: 'scene-information',
        name: '场景信息',
        route: '/scene/information'
      },
      {
        index: 'scene-device',
        name: '设备列表',
        route: '/scene/device'
      },
      {
        index: 'scene-location',
        name: '场景布局',
        route: '/scene/location'
      },
    ],
    routerPath: ''
  })
  const { selectedItem, items, routerPath } = toRefs(state)
  
  watchEffect(() => {
    const path = router.currentRoute.value.path
    routerPath.value=path
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
      let query={}
      if(selectedItemData.route==='/meta/domain/setting'){
        query['mode']='create'
      }
      router.push({
        path: selectedItemData.route,
        query: query
      })
    }
  }
  </script>
  <style scoped>
  .nav {
    width: 200px;
    height: 100%;
    background-color: #0464e4;
    color: white;
  }
  .logo{
    font-size: 20px;
    padding-top: 10px;
    margin-left: 10px;
  }
  .nav-menu {
    display: flex;
    flex-direction: column;
  }
  
  .nav-item {
    width: 100%;
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

  :deep(.nav-item .el-menu-item.is-active) {
    background-color: #e4ecfc;
    margin: 5px;
    border-radius: 5px;
  }

  :deep(.nav-item .el-menu-item:hover) {
    color: #0464e4;
  }

  :deep(.nav-item .el-sub-menu__title:hover) {
    color: #0464e4;
  }
  </style>
  