<template>
  <el-container style="width: 100%; height: 100%">
    <el-header style="padding: 0"><DesignHeader :applicationId="applicationId" :applicationName="applicationName" /></el-header>
    <el-container>
      <el-aside style="width: 80px"><DesignAsideBar :applicationId="applicationId" :applicationName="applicationName" /></el-aside>
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

interface State {
  applicationId: String;
  applicationName: String;
}

const state = reactive<State>({
  applicationId: '',
  applicationName: ''
})
const {applicationId, applicationName} = toRefs(state)

const router = useRouter()
watchEffect(() => {
  if (typeof router.currentRoute.value.query.applicationId === 'string') {
    applicationId.value = router.currentRoute.value.query.applicationId || ''
  }
  if (typeof router.currentRoute.value.query.applicationName === 'string') {
    applicationName.value = router.currentRoute.value.query.applicationName || ''
  }
})
</script>
