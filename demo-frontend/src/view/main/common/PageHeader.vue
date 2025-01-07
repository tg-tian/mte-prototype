<template>
  <div class="header-box">
    <div class="header-title">
      <el-icon class="header-icon"><Menu /></el-icon>
      <div>111</div>
    </div>
    <div></div>
  </div>
</template>


<script setup lang="ts">
/**
 * 大括号 {} 用于引入 命名导出，从模块中选择需要的特定内容。
 * 没有大括号时，是在引入 默认导出。
 * 在 @element-plus/icons-vue 中，Menu 是一个命名导出，因此需要加 {}。
 * */
import {Menu} from "@element-plus/icons-vue";
</script>

<style scoped>

.header-title{
  height: 50%;
  align-items: center;
  overflow: auto;
  display: flex;
}
.header-icon{
  color: #7F51C8;
  opacity: 0.8;
  margin: 10px;
  font-size: 30px;
}
.header-box {
  width: 100%;
  height: 60px;
  display: flex;
  background-color: #F1F5FB;
  flex-direction: row;
  align-items: center;
  overflow: auto;
  border-radius: 9px;
}
</style>

