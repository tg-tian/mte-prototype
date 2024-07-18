<template>
  <el-menu :default-active="selectedItem" collapse class="nav-menu" @select="handleMenuSelect">
    <el-sub-menu index="add">
      <template #title>
        <el-button type="primary" style="padding: 10px">+</el-button>
      </template>
      <el-menu-item-group title="流程">
        <el-menu-item index="0-1">新增流程</el-menu-item>
      </el-menu-item-group>
      <el-menu-item-group title="页面">
        <el-menu-item index="1-1">新增普通页面</el-menu-item>
        <el-menu-item index="1-2">新增表单</el-menu-item>
      </el-menu-item-group>
    </el-sub-menu>
    <el-menu-item index="index" class="nav-item">
      <el-icon><House /></el-icon>
      <template #title>设计</template>
    </el-menu-item>
    <el-menu-item index="process" class="nav-item">
      <el-icon><Connection /></el-icon>
      <template #title>功能</template>
    </el-menu-item>
    <el-menu-item index="page" class="nav-item">
      <el-icon><Document /></el-icon>
      <template #title>页面</template>
    </el-menu-item>
  </el-menu>
</template>
<script setup lang="ts">
import router from "@/router";
import {Connection, Document, House} from "@element-plus/icons-vue";

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
    router.push(selectedItemData.route)
  }
}
</script>

<style scoped>
.nav-menu {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
