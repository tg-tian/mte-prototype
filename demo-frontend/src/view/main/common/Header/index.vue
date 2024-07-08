<template>
  <div class="header">
    <div class="header-title">
      <img width="30" height="30" title="低代码" :src="logo" />
      <div style="line-height: 32px; padding-left: 10px">场景化低代码开发工具</div>
    </div>
    <div class="header-right">
      <el-button link style="margin-right: 20px;display: flex;color: white">
        <el-icon style="margin-right: 2px; margin-top: 1px;" size="20" color="white"><DataBoard /></el-icon>
      </el-button>
      <div style="margin-right: 20px">
        <el-dropdown>
          <el-button link style="display: flex;color: white">
            <el-icon style="margin-right: 2px; margin-top: 1px;" size="20" color="white"><Setting /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>系统设置</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div>
        登录/注册
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import logo from '../../../../assets/logo.png'
import {Setting, DataBoard} from "@element-plus/icons-vue";

</script>

<style scoped>
.header {
  width: 100%;
  height: 100%;
  background-color: #50a5fb;
  color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  text-align: center;
   /*color: #337ecc; */
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
  font-size: 1rem;
  color: white;
  align-items: center;
  display: flex;
}
</style>
