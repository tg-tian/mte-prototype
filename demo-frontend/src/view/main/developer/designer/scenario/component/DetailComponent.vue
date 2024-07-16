<template>
  <div class="scenario-subtitle" style="display: flex;justify-content: space-between">
    <div id="业务组件">业务组件</div>
    <el-button type="primary">新增业务组件</el-button>
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
