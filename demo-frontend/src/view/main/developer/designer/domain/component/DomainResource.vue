<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <div id="资源列表">资源列表</div>
    <el-button type="primary">新增资源</el-button>
  </div>
  <div class="domain-content">
    <el-tabs type="border-card">
      <el-tab-pane label="设备">
        <ResourceDevice :resourceId="resourceId" :resourceName="resourceName"/>
      </el-tab-pane>
      <el-tab-pane label="UI组件">
        <ResourceUI :resourceId="resourceId" :resourceName="resourceName"/>
      </el-tab-pane>
      <el-tab-pane label="流程">
        <ResourceProcess :resourceId="resourceId" :resourceName="resourceName"/>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script setup lang = "ts">
import ResourceDevice from "@/view/main/developer/designer/domain/component/ResourceDevice.vue";
import ResourceUI from "@/view/main/developer/designer/domain/component/ResourceUI.vue";
import ResourceProcess from "@/view/main/developer/designer/domain/component/ResourceProcess.vue";
interface State{

  resourceId: String;
  resourceName: String;
}

const state = reactive<State>({
  resourceId: '',
  resourceName: ''
})

const {resourceId , resourceName} = toRefs(state)
</script>

<style scoped>

</style>