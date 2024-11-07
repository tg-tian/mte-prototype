<template>
  <div class="application-title">{{applicationName}}应用设计</div>
  当前领域：智慧楼宇<br>
  当前场景：交叉二号楼
</template>
<script setup lang="ts">
interface State{
  applicationId: String;
  applicationName: String;
}
const state = reactive<State>({
  applicationId: '',
  applicationName: '',
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
<style scoped>
.application-title{
  margin-top: 10px;
  margin-bottom: 10px;
  font-size: 20px;
  font-weight: bold;
  color: #606266;
}
</style>
