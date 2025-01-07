<template>
  <PageHeader :title="applicationName+'——全局变量列表'" :button-group="buttonGroup" @button-click="handleGlobalVariableClick"/>
  <div class="body-box">
  </div>
</template>

<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";

const router = useRouter()
const buttonGroup = [
  {
    code: 'newGlobalVariable',
    name: '新增全局变量',
    type: 'primary'
  },
]
interface State {
  applicationId: String,
  applicationName: String,
}

const  state = reactive<State>({
  applicationId:"",
  applicationName:"",
})

const  {applicationId,applicationName} = toRefs(state)

watchEffect(() => {
  if (typeof router.currentRoute.value.query.applicationId === 'string') {
    applicationId.value = router.currentRoute.value.query.applicationId || ''
  }
  if (typeof router.currentRoute.value.query.applicationName === 'string') {
    applicationName.value = router.currentRoute.value.query.applicationName || ''
  }
})
const handleGlobalVariableClick=()=>{
  console.log("create the GlobalVariable");
}
</script>

<style scoped>

</style>