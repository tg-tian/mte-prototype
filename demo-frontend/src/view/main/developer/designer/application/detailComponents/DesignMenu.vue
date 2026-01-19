<template>
  <PageHeader :title="applicationName+'——菜单管理'" :button-group="buttonGroup" @button-click="handleMenuClick"/>
  <div class="body-box">
  </div>
</template>

<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";

const router = useRouter()
const buttonGroup = [
  {
    code: 'newMenu',
    name: '新增菜单',
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
const handleMenuClick=()=>{
  console.log("create the Menu");
}
</script>

<style scoped>

</style>