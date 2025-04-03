<template>
  <div class="scene-list-container">
    <div class="scene-header">
      <h2>场景列表 <small v-if="currentDomain">- {{ currentDomain.name }}</small></h2>
      <el-button type="primary" @click="navigateToSceneSetting()">创建场景</el-button>
    </div>
    
    <el-card class="scene-search">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="场景名称">
          <el-input v-model="searchForm.name" placeholder="请输入场景名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="活跃" value="active"></el-option>
            <el-option label="非活跃" value="inactive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-table
      v-loading="sceneStore.loading"
      :data="filteredScenes"
      style="width: 100%; margin-top: 20px"
      border
    >
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="场景名称" min-width="150"></el-table-column>
      <el-table-column prop="description" label="描述" min-width="200"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="120"></el-table-column>
      <el-table-column prop="deviceCount" label="设备数量" width="100"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
            {{ scope.row.status === 'active' ? '活跃' : '非活跃' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button type="primary" size="small" @click="navigateToSceneSetting(scope.row)">编辑</el-button>
          <el-button type="success" size="small" @click="handleViewScene(scope.row)">进入场景</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, toRefs } from 'vue'
import { useSceneStore } from '@/store/scene'
import { useDomainStore } from '@/store/domain'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const sceneStore = useSceneStore()
const domainStore = useDomainStore()

// 状态
const state = reactive({
  searchForm: {
    name: '',
    status: ''
  },
  dialogVisible: false,
  currentId: null
})

const { searchForm, dialogVisible, currentId } = toRefs(state)

// 获取当前域
const currentDomain = computed(() => {
  return domainStore.currentDomain
})

// 过滤后的场景列表
const filteredScenes = computed(() => {
  if (!sceneStore.scenes) return []
  
  return sceneStore.scenes.filter((scene: any) => {
    const nameMatch = !searchForm.value.name || scene.name.toLowerCase().includes(searchForm.value.name.toLowerCase())
    const statusMatch = !searchForm.value.status || scene.status === searchForm.value.status
    return nameMatch && statusMatch
  })
})

// 初始化
onMounted(async () => {
  // 获取domainId参数
  const domainId = parseInt(route.query.domainId as string)
  if (domainId) {
    await sceneStore.fetchScenes(domainId)
  } else {
    await sceneStore.fetchScenes()
  }
})

// 搜索处理
const handleSearch = () => {
  // 过滤是在计算属性中完成的
}

// 重置搜索
const resetSearch = () => {
  searchForm.value.name = ''
  searchForm.value.status = ''
}

// 导航到场景设置页面
const navigateToSceneSetting = (scene?: any) => {
  const domainId = parseInt(route.query.domainId as string) || 
                  (currentDomain.value ? currentDomain.value.id : null)
                  
  if (!domainId) {
    ElMessage.warning('请先选择一个领域')
    return
  }
  
  if (scene) {
    // For editing, set the current scene and navigate with scene ID
    sceneStore.setCurrentScene(scene)
    router.push(`/domain/scene/setting?sceneId=${scene.id}&domainId=${domainId}&mode=edit`)
  } else {
    // For creation, navigate to setting page with domain ID
    router.push(`/domain/scene/setting?domainId=${domainId}&mode=create`)
  }
}

// 查看场景
const handleViewScene = (row: any) => {
  sceneStore.setCurrentScene(row)
  router.push(`/scene/information?sceneId=${row.id}`)
}

// 删除场景
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除场景 "${row.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(async () => {
    try {
      await sceneStore.deleteScene(row.id)
      ElMessage.success('删除成功')
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
  .catch(() => {
    // 用户取消操作
  })
}
</script>

<style scoped>
.scene-list-container {
  padding: 20px;
}

.scene-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.scene-search {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}
</style>
