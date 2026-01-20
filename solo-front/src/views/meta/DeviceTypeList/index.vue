<template>
  <div class="devicetype-list-container">
    <div class="header-section">
      <div class="title-wrapper">
        <h2 class="main-title">设备类型管理</h2>
        <p class="sub-title">定义和管理平台支持的物联网设备元模型</p>
      </div>
      <el-button type="primary" class="create-btn" @click="navigateToDeviceTypeSetting()">
        <el-icon><Plus /></el-icon>创建设备类型
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="模型名称">
          <el-input 
            v-model="searchForm.modelName" 
            placeholder="请输入模型名称" 
            clearable
            @keyup.enter="handleSearch"
            style="width: 240px"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-table
      v-loading="deviceTypeStore.loading"
      :data="formattedDeviceTypes"
      style="width: 100%; margin-top: 24px"
      class="premium-table"
      :header-cell-style="{ background: '#f5f7fa', color: '#606266', fontWeight: 'bold' }"
    >
      <el-table-column prop="id" label="ID" width="100" align="center" />
      <el-table-column prop="modelName" label="模型名称" min-width="180">
        <template #default="{ row }">
          <span class="model-name-text">{{ row.modelName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="品类" width="150" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.category" size="small" effect="light" round>
            {{ row.category }}
          </el-tag>
          <span v-else style="color: #c0c4cc">-</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" min-width="180" align="center" />
      <el-table-column prop="updateTime" label="更新时间" min-width="180" align="center" />
      <el-table-column label="操作" width="250" fixed="right" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="navigateToDeviceTypeSetting(scope.row)">编辑</el-button>
          <el-button link type="success" @click="viewJson(scope.row)">查看JSON</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="searchForm.current"
        v-model:page-size="searchForm.size"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="deviceTypeStore.deviceTypePage.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- JSON查看对话框 -->
    <el-dialog v-model="jsonDialogVisible" title="设备类型JSON" width="60%">
      <pre class="json-viewer">{{ formattedDeviceTypeJson }}</pre>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="copyJson">复制</el-button>
          <el-button @click="jsonDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed, onMounted, ref } from 'vue'
import { Plus, Search } from '@element-plus/icons-vue'
import { useDeviceTypeStore } from '@/store/deviceType'
import { getDeviceTypeById } from '@/api/deviceType'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { DeviceType } from '@/types/models'

const router = useRouter()
const deviceTypeStore = useDeviceTypeStore()

// JSON对话框相关状态
const jsonDialogVisible = ref(false)
const jsonDeviceType = ref<DeviceType | null>(null)

// 格式化JSON - 只显示model部分，和编辑页面保持一致
const formattedDeviceTypeJson = computed(() => {
  if (!jsonDeviceType.value) return ''
  // 只显示model部分，和编辑页面的formattedModelJson保持一致
  return JSON.stringify(jsonDeviceType.value.model || {}, null, 2)
})

const searchForm = reactive({
  current: 1,
  size: 10,
  modelName: ''
})

const formattedDeviceTypes = computed(() => {
  return deviceTypeStore.deviceTypePage.records.map((deviceType: any) => {
    return {
      ...deviceType,
      updateTime: deviceType.updateTime?.split('.')[0].replace('T', ' '),
      createTime: deviceType.createTime?.split('.')[0].replace('T', ' ')
    }
  })
})

onMounted(() => {
  handleSearch()
})

const handleSearch = () => {
  deviceTypeStore.fetchDeviceTypePage(searchForm)
}

const resetSearch = () => {
  searchForm.modelName = ''
  searchForm.current = 1
  handleSearch()
}

const handleSizeChange = (val: number) => {
  searchForm.size = val
  searchForm.current = 1
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  searchForm.current = val
  handleSearch()
}

const navigateToDeviceTypeSetting = (deviceType?: DeviceType) => {
  if (deviceType) {
    deviceTypeStore.setCurrentDeviceType(deviceType)
    router.push(`/meta/devicetype/setting?deviceTypeId=${deviceType.id}&mode=edit`)
  } else {
    router.push('/meta/devicetype/setting?mode=create')
  }
}

const handleDelete = (row: DeviceType) => {
  ElMessageBox.confirm(
    `确定要删除设备类型 "${row.modelName}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(async () => {
    try {
      await deviceTypeStore.deleteDeviceType(row.id)
      ElMessage.success('删除成功')
      handleSearch()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
  .catch(() => {})
}

// 查看设备类型JSON
const viewJson = async (deviceType: DeviceType) => {
  try {
    // 如果列表中的数据不完整，需要重新获取完整数据
    if (!deviceType.model) {
      const res: any = await getDeviceTypeById(deviceType.id)
      jsonDeviceType.value = res.data || deviceType
    } else {
      jsonDeviceType.value = deviceType
    }
    jsonDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取设备类型数据失败')
  }
}

// 复制JSON
const copyJson = () => {
  navigator.clipboard.writeText(formattedDeviceTypeJson.value)
    .then(() => {
      ElMessage.success('JSON已复制到剪贴板')
    })
    .catch(err => {
      console.error('复制失败:', err)
      ElMessage.error('复制失败')
    })
}
</script>

<style scoped>
.devicetype-list-container {
  padding: 24px;
  background: #fcfcfd;
  min-height: calc(100vh - 84px);
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.main-title {
  font-size: 24px;
  font-weight: 600;
  color: #1a1b1e;
  margin: 0 0 8px 0;
}

.sub-title {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

.search-card {
  border: none;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.premium-table {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  border: none;
}

.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}

.model-name-text {
  font-weight: 500;
  color: #303133;
}

:deep(.el-table__row) {
  transition: all 0.3s;
  height: 64px;
}

:deep(.el-table__row:hover) {
  background-color: #f5f7fa !important;
  transform: translateY(-1px);
}

.json-viewer {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 4px;
  overflow-x: auto;
  max-height: 500px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.6;
  color: #303133;
}
</style>