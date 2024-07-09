<template>
  <div>
      <el-tabs v-model="activeName" class="workspace-tabs" @tab-click="handleClick">
        <el-tab-pane label="我的领域" name="domain">我的领域</el-tab-pane>
        <el-tab-pane label="我的组件" name="component">业务组件/UI组件/设备组件</el-tab-pane>
      </el-tabs>
    <el-button type="primary" class="add-button">添加</el-button>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'

const activeName = ref('domain')

const handleClick = (tab: TabsPaneContext, event: Event) => {
}
</script>

<style scoped>
.add-button {
}
</style>

<style>
.el-tabs__item {
  font-size: 18px !important;
}
.el-tabs__nav-wrap:after {
  height: 0 !important;
}
</style>
