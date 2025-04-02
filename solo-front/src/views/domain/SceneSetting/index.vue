<template>
  <div class="scene-setting-container">
    <h2>场景定制</h2>
    <el-alert
      title="场景定制功能正在开发中"
      type="info"
      description="此页面将允许用户自定义场景的布局、设备和数据流"
      show-icon
      :closable="false"
    />
    
    <div class="setting-content">
      <el-tabs>
        <el-tab-pane label="基本信息">
          <el-form label-width="120px">
            <el-form-item label="场景名称">
              <el-input placeholder="请输入场景名称"></el-input>
            </el-form-item>
            <el-form-item label="场景描述">
              <el-input type="textarea" rows="3" placeholder="请输入场景描述"></el-input>
            </el-form-item>
            <el-form-item label="场景类型">
              <el-select placeholder="请选择场景类型">
                <el-option label="工厂车间" value="factory"></el-option>
                <el-option label="智慧城市" value="city"></el-option>
                <el-option label="医疗设施" value="hospital"></el-option>
                <el-option label="教育场所" value="education"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="设备类型定义">
          <el-empty description="暂无设备类型" />
        </el-tab-pane>
        
        <el-tab-pane label="数据流定义">
          <el-empty description="暂无数据流定义" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.scene-setting-container {
  padding: 20px;
}

.setting-content {
  margin-top: 20px;
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}
</style>
