<template>
  <div style="padding: 20px">
    <div>{{scenarioName}}-应用列表</div>
    <div style="margin-top: 20px">
      <el-table :data="tableData" stripe style="width: 60%" border>
        <el-table-column prop="applicationId" label="应用代码" />
        <el-table-column prop="applicationName" label="应用名" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="goToDetail(scope.row.applicationId, scope.row.applicationName)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
interface State {
  tableData: any[];
  scenarioName: String
}

const state = reactive<State>({
  tableData: [],
  scenarioName: ""
})
const { tableData, scenarioName } = toRefs(state)

const router = useRouter()

onActivated(() => {
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
      applicationId: "GuestReception",
      applicationName: "来访接待"
    }
  ]
}

const goToDetail = (applicationId: String, applicationName: String)=>{
  router.push({ path: '/application/detail', query: { applicationId: applicationId, applicationName: applicationName, scenarioName: scenarioName.value } })
}
</script>
