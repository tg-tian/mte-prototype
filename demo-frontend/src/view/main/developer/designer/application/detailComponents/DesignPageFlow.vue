<template>
  <PageHeader :title="applicationName+'——页面流列表'" :button-group="buttonGroup" @button-click="handlePageFlowClick"/>
  <div class="body-box">
  </div>
</template>

<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";

const router = useRouter()
const buttonGroup = [
  {
    code: 'newPageFlow',
    name: '新增页面流',
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
const handlePageFlowClick=()=>{
  console.log("create the pageFlow");
}
</script>

<style scoped>

</style>