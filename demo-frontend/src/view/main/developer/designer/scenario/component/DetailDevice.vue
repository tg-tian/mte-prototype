<template>
  <div class="scenario-subtitle" style="display: flex;justify-content: space-between">
    <div id="设备">设备</div>
    <el-button type="primary">新增设备</el-button>
  </div>
  <div class="scenario-content">

  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  scenarioId: String,
  scenarioName: String
});
</script>
