<template>
  <el-container style="width: 100%; height: 100%">
    <el-header style="padding: 0"><DesignHeader :applicationId="applicationId" :applicationName="applicationName" /></el-header>
    <el-container>
      <el-aside style="width: 200px"><DesignAsideBar :applicationId="applicationId" :applicationName="applicationName" /></el-aside>

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
import {useApplicationStore} from "@/store/modules/applicationStore";

interface State {
  applicationId: string;
  applicationName: string;
}

const state = reactive<State>({
  applicationId: '',
  applicationName: ''
})
const {applicationId, applicationName} = toRefs(state)

const router = useRouter()
const applicationStore = useApplicationStore()
watchEffect(() => {
  const routerQuery = router.currentRoute.value.query
  if (typeof routerQuery.applicationId === 'string' && routerQuery.applicationId !== '') {
    applicationId.value = routerQuery.applicationId || ''
  }else {
    applicationId.value = applicationStore.getApplicationId
  }
  if (typeof routerQuery.applicationName === 'string' && routerQuery.applicationName !== '') {
    applicationName.value = routerQuery.applicationName || ''
  }else {
    applicationName.value = applicationStore.getApplicationName
  }
})
</script>
