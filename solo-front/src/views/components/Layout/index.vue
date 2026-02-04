<template>
    <div class="common-layout">
      <el-container class="app-container">
        <el-container class="main-wrapper">
          <el-header class="app-header">
            <Header />
          </el-header>
          <el-main class="app-main">
            <div class="content-view">
              <RouterView v-slot="{ Component }">
                <keep-alive>
                  <component :is="Component" />
                </keep-alive>
              </RouterView>
            </div>
          </el-main>
        </el-container>
      </el-container>
    </div>
  </template>
  
  <script setup lang="ts">
  import Header from '../Header/index.vue'
  </script>
  
  <style scoped>
  .common-layout {
    width: 100vw;
    height: 100vh;
    overflow: hidden;
  }

  .app-container {
    height: 100%;
  }

  .aside-menu {
    background-color: #0454c4;
    transition: width 0.3s;
    overflow: hidden;
    height: 100%;
  }

  .main-wrapper {
    height: 100%;
    display: flex;
    flex-direction: column;
    background-color: #f5f7fa;
  }

  .app-header {
    background-color: white;
    padding: 0;
    height: 60px !important;
    border-bottom: 1px solid #e4e7ed;
    flex-shrink: 0;
  }

  .app-main {
    flex: 1;
    padding: 0;
    overflow: hidden;
    display: flex;
    flex-direction: column;
  }

  .content-view {
    flex: 1;
    overflow-y: auto;
    padding: 24px;
    box-sizing: border-box;
  }
  </style>
  
  