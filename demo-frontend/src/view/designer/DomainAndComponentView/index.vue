<template>
  <div style="position: relative">
      <el-tabs v-model="activeName" class="workspace-tabs" @tab-click="handleClick">
        <el-tab-pane label="我的领域" name="domain"><MyDomain /></el-tab-pane>
        <el-tab-pane label="我的组件" name="component"><MyComponent /></el-tab-pane>
      </el-tabs>
    <div class="add-button">
      <el-dropdown>
        <el-button type="primary">
          <el-icon size="18" color="white"><Plus /></el-icon>
          &nbsp;添加</el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>添加领域</el-dropdown-item>
            <el-dropdown-item>添加组件</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
import {Plus} from "@element-plus/icons-vue";
import MyDomain from "./components/MyDomain.vue";
import MyComponent from "./components/MyComponent.vue";

const activeName = ref('domain')

const handleClick = (tab: TabsPaneContext, event: Event) => {
}
</script>

<style scoped>
.add-button {
  position: absolute;
  right: 40px;
  top: 4px;
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
