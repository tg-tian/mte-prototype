<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <div id="模板列表">模板列表</div>
  </div>
  <div class="domain-content">
    <el-tabs type="border-card">
      <el-tab-pane label="设备类型">
        <ResourceDevice :domainId="domainId" :domainName="domainName"/>
      </el-tab-pane>
      <el-tab-pane label="UI组件">
        <ResourceUI :domainId="domainId" :domainName="domainName"/>
      </el-tab-pane>
      <el-tab-pane label="流程">
        <ResourceProcess :domainId="domainId" :domainName="domainName"/>
      </el-tab-pane>
    </el-tabs>
  </div>

</template>

<script setup lang = "ts">
import ResourceDevice from "@/view/main/developer/designer/domain/component/ResourceDevice.vue";
import ResourceUI from "@/view/main/developer/designer/domain/component/ResourceUI.vue";
import ResourceProcess from "@/view/main/developer/designer/domain/component/ResourceProcess.vue";

const props = defineProps({
  domainId: String,
  domainName: String
})
</script>

<style scoped>

</style>
