<template>
  <div style="padding: 20px">
    <div>{{scenarioName}}-资源列表</div>
    <div style="margin-top: 20px; width: 60%">
      <div style="display: flex; justify-content: space-between;">
        <div>设备列表</div>
        <el-button type="primary">添加设备</el-button>
      </div>
      <el-table :data="tableData" stripe style="margin-top: 10px" border>
        <el-table-column prop="deviceName" label="设备名" />
        <el-table-column prop="deviceType" label="设备类型" />
        <el-table-column prop="deviceService" label="设备厂商" />
        <el-table-column prop="uri" label="uri" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" disabled @click="goToDetail(scope.row.deviceId)">查看详情</el-button>
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
      deviceId: "deviceId",
      deviceName: "咖啡机器人A",
      deviceType: '咖啡机器人',
      deviceService: 'AService',
      uri: 'http://10.176.34.96:2030?action='
    }
  ]
}

const goToDetail = (deviceId: String)=>{

}
</script>
