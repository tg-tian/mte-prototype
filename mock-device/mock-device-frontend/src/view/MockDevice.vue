<template>
  <div style="display: flex;width: 100%">
    <div style="width: 50%;margin: 10px">
      <DeviceA></DeviceA>
    </div>
    <div style="width: 50%;margin: 10px">
      <DeviceB></DeviceB>
    </div>
  </div>
</template>
<script setup lang="ts">
import DeviceA from "./DeviceA.vue";
import DeviceB from "./DeviceB.vue";
</script>
