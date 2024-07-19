<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <div id="资源列表">资源列表</div>
    <el-button @click="dialogVisible = true" type="primary">添加资源</el-button>
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
  <el-dialog v-model = "dialogVisible" title="新增资源" width="50%">
    <div style="padding: 20px">
      <el-form

      >
        <el-form-item>添加资源</el-form-item>
      </el-form>
    </div>
  </el-dialog>

</template>

<script setup lang = "ts">
import ResourceDevice from "@/view/main/developer/designer/domain/component/ResourceDevice.vue";
import ResourceUI from "@/view/main/developer/designer/domain/component/ResourceUI.vue";
import ResourceProcess from "@/view/main/developer/designer/domain/component/ResourceProcess.vue";


interface State{

  resourceId: String;
  resourceName: String;
  dialogVisible:boolean;
}

const state = reactive<State>({
  resourceId: '',
  resourceName: '',
  dialogVisible:false,
})

const {resourceId , resourceName, dialogVisible} = toRefs(state)
</script>

<style scoped>

</style>