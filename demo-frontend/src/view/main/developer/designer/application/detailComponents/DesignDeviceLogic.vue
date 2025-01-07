<template>
  <PageHeader :title="applicationName+'——设备逻辑列表'" :button-group="buttonGroup" @button-click="handleHeaderButtonClick"/>
  <div class="body-box">
  </div>
</template>

<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";

const router = useRouter()
const buttonGroup = [
  {
    code: 'newDeviceLogic',
    name: '新增设备逻辑',
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
const handleHeaderButtonClick=()=>{
  console.log("create the device logic")
}
</script>

<style scoped>

</style>