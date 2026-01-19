<template xmlns="">
  <div class="common-layout">
    <el-container style="width: 100%; height: 100%">
      <el-header style="padding: 0"><Header/></el-header>
      <el-container>
        <el-aside width="200px" v-if="isAuthenticated"><AsideBar /></el-aside>
        <el-main style="padding: 30px 50px 50px;">
          <RouterView v-slot="slotProps">
            <keep-alive>
              <component :is="slotProps.Component"></component>
            </keep-alive>
          </RouterView>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import AsideBar from '../ASideBar/index.vue'
import Header from '../Header/index.vue'
import {useUserStore} from "@/store/modules/userStore";
import {storeToRefs} from "pinia";

const userStore = useUserStore()
const { isAuthenticated } = storeToRefs(userStore)
</script>

<style scoped>
.common-layout {
  width: 100%;
  height: 100%;
}
</style>

