<template>
    <el-button type="primary" style="float: right;margin-bottom: 10px">绑定设备类型</el-button>
    <el-empty description="暂无组件" v-if="filteredDeviceTypes.length===0"/>
    <el-table
      v-loading="deviceTypeStore.loading"
      :data="filteredDeviceTypes"
      style="width: 100%; margin-top: 20px"
      border
      v-else
    >
      <el-table-column prop="code" label="设备类型编码" width="150"></el-table-column>
      <el-table-column prop="name" label="设备类型名称" min-width="150"></el-table-column>
      <el-table-column prop="description" label="设备类型描述" min-width="200"></el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button type="primary" size="small" @click="navigateToDeviceTypeSetting(scope.row)">查看</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
</template>

<script setup lang="ts">
import { useDeviceTypeStore } from '@/store/deviceType'
const route = useRoute()
const router = useRouter()
const deviceTypeStore = useDeviceTypeStore()

const domainId = computed(() => {
  return route.query.domainId ? parseInt(route.query.domainId as string) : null
})

// 初始化
onMounted(async () => {
    try {
        await deviceTypeStore.fetchDeviceTypes(parseInt(route.query.domainId as string))
    } catch (error) {
        console.error('加载设备类型失败:', error)
    }
})

watch([() => route.query.domainId], async ([newDomainId]) => {
    try {
        await deviceTypeStore.fetchDeviceTypes(newDomainId ? parseInt(newDomainId as string) : undefined)
    } catch (error) {
        console.error('加载设备类型失败:', error)
    }
})

// 过滤后的领域列表
const filteredDeviceTypes = computed(() => {
  if (!deviceTypeStore.deviceTypes) return []
  
  let deviceTypes = deviceTypeStore.deviceTypes.map((deviceType: any)=>{
    return {
      ...deviceType,
      updateTime: deviceType.updateTime?.split('.')[0].replace('T', ' '),
      createTime: deviceType.createTime?.split('.')[0].replace('T', ' ')
    }
  })

  return deviceTypes
})

// 导航到设备类型设置页面
const navigateToDeviceTypeSetting = (deviceType?: any) => {
  if (deviceType) {
    // 设备类型详情
    deviceTypeStore.setCurrentDeviceType(deviceType)
    router.push(`/meta/devicetype/setting?deviceTypeId=${deviceType.id}&mode=edit`)
  }
}

// 删除领域绑定信息
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要取消绑定设备类型 "${row.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(async () => {
    try {
      await deviceTypeStore.unbindingDeviceType(row.id, domainId.value)
      ElMessage.success('取消绑定成功')
    } catch (error) {
      ElMessage.error('取消绑定失败')
    }
  })
  .catch(() => {
    // 用户取消操作
  })
}
</script>