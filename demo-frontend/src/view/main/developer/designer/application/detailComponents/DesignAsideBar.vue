<template>

    <el-menu :default-active="selectedItem"  class="nav-menu" @select="handleMenuSelect">
      <!--el-sub-menu index="add">
        <template #title>
          <el-button type="primary" style="padding: 10px">+</el-button>
        </template>
        <el-menu-item-group title="功能">
          <el-menu-item index="0-1">新增功能服务</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="页面">
          <el-menu-item index="1-1">新增页面</el-menu-item>
        </el-menu-item-group>
      </el-sub-menu-->
      <el-menu-item index="index" class="nav-item">
        <template #title>
          <el-icon><House /></el-icon>
          <div class="menu-item">应用信息</div>
        </template>
      </el-menu-item>
      <el-menu-item index="process" class="nav-item">
        <template #title>
          <el-icon><Connection /></el-icon>
          <div class="menu-item">业务逻辑</div>
        </template>
      </el-menu-item>
      <el-menu-item index="4" class="nav-item">
        <template #title>
          <el-icon><Cpu /></el-icon>
          <div class="menu-item">设备逻辑</div>
        </template>
      </el-menu-item>
      <el-menu-item index="page" class="nav-item">
        <template #title>
          <el-icon><Document /></el-icon>
          <div class="menu-item">页面管理</div>
        </template>
      </el-menu-item>
      <el-menu-item index="3" class="nav-item">
        <template #title>
          <el-icon><More /></el-icon>
          <div class="menu-item">菜单管理</div>
        </template>
      </el-menu-item>
      <el-menu-item index="2" class="nav-item">
        <template #title>
          <el-icon><Share /></el-icon>
          <div class="menu-item">页面流管理</div>
        </template>
      </el-menu-item>
      <el-menu-item index="1" class="nav-item">
        <template #title>
          <el-icon><Eleme /></el-icon>
          <div class="menu-item">全局变量管理</div>
        </template>
      </el-menu-item>
      <el-menu-item index="quality_assurance" class="nav-item">
        <template #title>
          <MdiShieldBug/>
          <div class="menu-item">质量保障</div>
        </template>
      </el-menu-item>
    </el-menu>

</template>
<script setup lang="ts">
import router from "@/router";
import {Connection, Document, House,Cpu,More,Share,Eleme} from "@element-plus/icons-vue";
import MdiShieldBug from "@/view/main/common/Icon/MdiShieldBug.vue";

const props = defineProps({
  applicationId: String,
  applicationName: String
});

interface State {
  selectedItem: string
  items: any;
}

const state = reactive<State>({
  selectedItem: '',
  items: [
    {
      index: 'index',
      name: '设计首页',
      route: '/design/index'
    },
    {
      index: 'process',
      name: '流程目录',
      route: '/design/process'
    },
    {
      index: 'page',
      name: '页面目录',
      route: '/design/page'
    },
    {
      index: 'quality_assurance',
      name: '质量保障',
      route:'/design/quality_assurance'
    }
  ]
})
const { selectedItem, items } = toRefs(state)

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
    router.push({path: selectedItemData.route, query: { applicationId: props.applicationId, applicationName: props.applicationName}})
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
.menu-item {
  width: 100px;
  text-align: center;
  font-size: 16px;
}
</style>
