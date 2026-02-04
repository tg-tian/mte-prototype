<template>
  <div class="header">
    <!-- 头部内容已移除 -->
  </div>
</template>
  
<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

// 用户组件相关代码已移除
</script>
  
<style>
.header {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ecf4fc;
}

.header-title {
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  font-family: 'Courier New', Courier, monospace;
  display: flex;
  justify-content: space-evenly;
  margin-left: 20px;
}

.header-right {
  margin-right: 20px;
  display: flex;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}
</style>
