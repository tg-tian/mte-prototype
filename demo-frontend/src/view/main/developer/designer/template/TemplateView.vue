<template>
  <div>
    <div class="header-panel">
      <div class="header-center">
        <div class="header-title">在模板库中寻找符合场景的模板</div>
        <div class="header-search">
          <el-input placeholder="在模板库中搜索" class="search-input">
            <template #prefix>
              <el-icon class="el-input__icon" style="font-size: 20px;color: #606266"><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>
    </div>
    <div style="display: flex;justify-content: space-between">
      <div style="font-weight: bold;font-size: 18px">所有模板</div>
      <el-button type="primary">添加模板</el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {Search} from "@element-plus/icons-vue";
</script>

<style scoped>
.header-panel {
  width: 100%;
  height: 300px;
  margin-bottom: 20px;
  border-radius: 10px;
  background-image: url('../../../../../assets/images/header-image.jpg');
  background-repeat: no-repeat;
  background-size: cover;
}

.header-center {
  width: 70%;
  margin-left: 15%;
  padding-top: 80px;
}

.header-search {
  width: 80%;
  margin-left: 10%;
  margin-top: 20px;
  text-align: center;
}

.search-input {
  height: 45px;
}

.header-title {
  font-size: 40px;
  color: white;
  font-family: 'Arial', sans-serif;
}
</style>
