<template>
  <div>
    <PageHeader :title="applicationName+'——应用信息'" :button-group="buttonGroup" @button-click="handleHeaderButtonClick"/>
    <div class="application-title">{{applicationName}}应用设计</div>
    <div></div>
    领域：智慧楼宇<br>场景：交叉二号楼
  </div>
  <div class="button-group">
    <ElButton type="primary">预览</ElButton>
    <ElButton type="success">发布</ElButton>
    <ElButton type="warning">代码检查</ElButton>
    <ElButton type="primary" @click="goToCodeServer">代码编辑</ElButton>
  </div>
</template>
<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";

const buttonGroup = [
  {
    code: 'preview',
    name: '预览',
    type: 'primary'
  },
  {
    code: 'publish',
    name: '发布',
    type: 'success'
  },
  {
    code: 'code-view',
    name: '代码检查',
    type: 'warning'
  },
  {
    code: 'code-server',
    name: '代码编辑',
    type: 'primary'
  },
]

const handleHeaderButtonClick = (code: string)=>{
  if (code === 'code-server'){
    goToCodeServer()
  }
}

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

const goToCodeServer = ()=>{
  window.open("http://139.196.147.52:5200/?folder=/home/coder", '_blank');
}
</script>
<style scoped>
.application-title{
  margin-top: 10px;
  margin-bottom: 10px;
  font-size: 20px;
  font-weight: bold;
  color: #606266;
}
.button-group {
  display: flex;
}
</style>
