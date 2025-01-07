<script setup lang="ts">
  import { RouterView } from 'vue-router'
  import '@/assets/style.css'
</script>

<template>
  <RouterView v-slot="slotProps">
    <keep-alive>
      <component :is="slotProps.Component"></component>
    </keep-alive>
  </RouterView>
</template>

<style scoped>

</style>
