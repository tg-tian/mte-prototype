<template>
  <div class="scenario-subtitle" style="display: flex;justify-content: space-between">
    <div id="UI组件">UI组件</div>
    <el-button type="primary">新增UI组件</el-button>
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
