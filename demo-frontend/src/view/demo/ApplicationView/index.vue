<template>
  <div style="padding: 20px">
    <div>{{scenarioName}}-{{applicationName}}应用-功能列表</div>
    <div style="margin-top: 20px">
      <el-table :data="tableData" stripe style="width: 60%" border>
        <el-table-column prop="processId" label="功能代码" />
        <el-table-column prop="processName" label="功能名" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="goToDetail(scope.row.processId, scope.row.processName)">打开</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
interface State {
  tableData: any[];
  applicationName: String;
  scenarioName: String;
}

const state = reactive<State>({
  tableData: [],
  applicationName: '',
  scenarioName: ''
})
const { tableData, applicationName, scenarioName } = toRefs(state)

const router = useRouter()

onActivated(() => {
  if (typeof router.currentRoute.value.query.applicationName === 'string') {
    applicationName.value = router.currentRoute.value.query.applicationName || ''
  }
  if (typeof router.currentRoute.value.query.scenarioName === 'string') {
    scenarioName.value = router.currentRoute.value.query.scenarioName || ''
  }
})

onMounted(()=>{
  fetchData()
})

const fetchData = ()=>{
  tableData.value = [
    {
      processId: "ConferenceService",
      processName: "会议服务"
    }
  ]
}

const goToDetail = (processId: String, processName: String)=>{
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  router.push({ path: '/demo/page', query: { processId: processId, processName: processName, applicationName: applicationName.value, scenarioName: scenarioName.value } })
}
</script>
