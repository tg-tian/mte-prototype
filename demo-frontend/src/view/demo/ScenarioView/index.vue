<template>
  <div style="padding: 20px">
    <div>场景列表</div>
    <div style="margin-top: 20px">
      <el-table :data="tableData" stripe style="width: 60%" border>
        <el-table-column prop="scenarioId" label="场景代码" />
        <el-table-column prop="scenarioName" label="场景名" />
        <el-table-column label="操作" width="300">
          <template #default="scope">
            <el-button type="primary" size="small" @click="goToResourceList(scope.row.scenarioId, scope.row.scenarioName)">查看资源</el-button>
            <el-button type="primary" size="small" @click="goToApplicationList(scope.row.scenarioId, scope.row.scenarioName)">查看应用</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
interface State {
  tableData: any[]
}

const state = reactive<State>({
  tableData: []
})
const { tableData } = toRefs(state)

const router = useRouter()

onMounted(()=>{
  fetchData()
})

const fetchData = ()=>{
  tableData.value = [
    {
      scenarioId: "BuildingA",
      scenarioName: "交叉二号楼"
    }
  ]
}

const goToApplicationList = (scenarioId: String, scenarioName: String)=>{
  router.push({ path: '/demo/applicationList', query: { scenarioId: scenarioId, scenarioName: scenarioName } })
}

const goToResourceList = (scenarioId: String, scenarioName: String)=>{
  router.push({ path: '/demo/resource', query: { scenarioId: scenarioId, scenarioName: scenarioName } })
}
</script>
