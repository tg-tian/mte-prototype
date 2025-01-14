<template>
  <div class="header">
    <div class="header-title" style="cursor: pointer" @click="router.push({path:'/'})">
      <img width="30" height="30" title="低代码" :src="logo" />
      <div style="line-height: 32px; padding-left: 10px">面向场景计算的低代码设计器</div>
    </div>
  </div>
</template>
<script setup lang="ts">
import logo from '../../../../../../assets/logo.png'
const router = useRouter()
</script>
