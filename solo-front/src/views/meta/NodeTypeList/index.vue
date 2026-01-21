<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title-group">
        <h2 class="page-main-title">节点类型列表</h2>
        <p class="page-sub-title">定义拓扑图中支持的各种物理或逻辑节点类型</p>
      </div>
      <el-button type="primary" size="large" @click="navigateToNodeTypeSetting()">
        <el-icon><Plus /></el-icon>创建节点类型
      </el-button>
    </div>
    
    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <template #header>
        <div class="search-header">
          <el-icon><Search /></el-icon>
          <span>筛选节点类型</span>
        </div>
      </template>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入节点名称" clearable style="width: 220px"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-table
      v-loading="loading"
      :data="filteredNodeTypes"
      style="width: 100%; margin-top: 24px"
      class="premium-table"
      :header-cell-style="{ background: '#f5f7fa', color: '#606266', fontWeight: 'bold' }"
    >
      <el-table-column prop="code" label="节点类型编码" width="180">
        <template #default="{ row }">
          <code class="code-text">{{ row.code }}</code>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="节点类型名称" min-width="150" />
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
      <el-table-column prop="updateTime" label="更新时间" width="180" align="center" />
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="navigateToNodeTypeSetting(scope.row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed, onMounted, toRefs, ref } from 'vue'
import { Plus, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)

// 状态
const state = reactive({
  searchForm: {
    name: ''
  },
  nodeTypes: [] as any[]
})

const { searchForm } = toRefs(state)

// 过滤后的节点类型列表
const filteredNodeTypes = computed(() => {
  if (!state.nodeTypes) return []
  
  return state.nodeTypes.filter((nodeType: any) => {
    const nameMatch = !searchForm.value.name || nodeType.name.toLowerCase().includes(searchForm.value.name.toLowerCase())
    return nameMatch
  }).map((nodeType: any)=>{
    return {
      ...nodeType,
      updateTime: nodeType.updateTime?.split('.')[0].replace('T', ' '),
      createTime: nodeType.createTime?.split('.')[0].replace('T', ' ')
    }
})
})

const handleSearch = () => {
  // Logic done in computed
}

const resetSearch = () => {
  searchForm.value.name = ''
}

const navigateToNodeTypeSetting = (nodeType?: any) => {
  if (nodeType) {
    router.push(`/meta/nodetype/setting?nodeTypeId=${nodeType.id}&mode=edit`)
  } else {
    router.push('/meta/nodetype/setting?mode=create')
  }
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除节点类型 "${row.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    ElMessage.success('删除成功')
  }).catch(() => {})
}
</script>

<style scoped>
.page-container {
  width: 100%;
}

.search-card {
  border: none;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.search-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.premium-table {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  border: none;
}

:deep(.el-table__row) {
  transition: all 0.3s;
  height: 64px;
}

:deep(.el-table__row:hover) {
  background-color: #f5f7fa !important;
  transform: translateY(-1px);
}

.code-text {
  background: #f0f2f5;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: inherit;
  color: #409eff;
}
</style>
