<template>
  <div class="domain-setting-container">
    <h2>领域定制</h2>
    <el-alert
      title="领域定制功能正在开发中"
      type="info"
      description="此页面将允许用户自定义领域的元数据、业务逻辑和界面组件"
      show-icon
      :closable="false"
    />
    
    <div class="setting-content">
      <el-tabs>
        <el-tab-pane label="基本信息">
          <el-form label-width="120px">
            <el-form-item label="领域名称">
              <el-input placeholder="请输入领域名称"></el-input>
            </el-form-item>
            <el-form-item label="领域描述">
              <el-input type="textarea" rows="3" placeholder="请输入领域描述"></el-input>
            </el-form-item>
            <el-form-item label="图标">
              <el-upload
                action="#"
                list-type="picture-card"
                :auto-upload="false"
              >
                <el-icon><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="模型定义">
          <el-empty description="暂无模型数据" />
        </el-tab-pane>
        
        <el-tab-pane label="业务流程">
          <el-empty description="暂无业务流程" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Plus } from '@element-plus/icons-vue'
</script>

<style scoped>
.domain-setting-container {
  padding: 20px;
}

.setting-content {
  margin-top: 20px;
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}
</style>
