<template>
  <el-container style="width: 100%; height: 100%">
    <el-header style="padding: 0"><DesignHeader /></el-header>
    <el-container>
      <el-aside style="width: 80px"><DesignAsideBar /></el-aside>
      <el-main style="padding: 30px 50px 50px;">
        <RouterView v-slot="slotProps">
          <keep-alive>
            <component :is="slotProps.Component"></component>
          </keep-alive>
        </RouterView>
      </el-main>
    </el-container>
  </el-container>
</template>
<script setup lang="ts">
import DesignHeader from "@/view/main/developer/designer/application/detailComponents/DesignHeader.vue";
import DesignAsideBar from "@/view/main/developer/designer/application/detailComponents/DesignAsideBar.vue";
</script>
