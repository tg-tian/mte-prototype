<template>
  <div class="domain-list-container">
    <div class="domain-header">
      <h2>领域平台列表</h2>
      <el-button type="primary" @click="navigateToDomainSetting()">创建领域</el-button>
    </div>
    
    <el-card class="domain-search">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="领域名称">
          <el-input v-model="searchForm.name" placeholder="请输入领域名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="已发布" value="1"></el-option>
            <el-option label="定制中" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-table
      v-loading="domainStore.loading"
      :data="filteredDomains"
      style="width: 100%; margin-top: 20px"
      border
    >
      <el-table-column prop="domainCode" label="领域编码" width="150"></el-table-column>
      <el-table-column prop="domainName" label="领域名称" min-width="100"></el-table-column>
      <el-table-column prop="domainDescription" label="描述" min-width="150"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120">
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="120">
      </el-table-column>
      <!-- <el-table-column prop="sceneCount" label="场景数量" width="100"></el-table-column> -->
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'info'">
            {{ scope.row.status === '1' ? '已发布' : '定制中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="350">
        <template #default="scope">
          <el-button type="primary" size="small" @click="navigateToDomainSetting(scope.row)">编辑</el-button>
          <el-button type="success" size="small" @click="navigateToDomainPlatform(scope.row)">进入领域</el-button>
          <el-button type="success" size="small" @click="handleViewScenes(scope.row)">查看场景</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, toRefs } from 'vue'
import { useDomainStore } from '@/store/domain'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const domainStore = useDomainStore()

// 状态
const state = reactive({
  searchForm: {
    name: '',
    status: ''
  }
})

const { searchForm } = toRefs(state)

// 过滤后的领域列表
const filteredDomains = computed(() => {
  if (!domainStore.domains) return []
  
  let domains = domainStore.domains.filter((domain: any) => {
    const nameMatch = !searchForm.value.name || domain.domainName.toLowerCase().includes(searchForm.value.name.toLowerCase())
    const statusMatch = !searchForm.value.status || domain.status === searchForm.value.status
    return nameMatch && statusMatch
  })
  domains = domains.map((domain: any)=>{
    return {
      ...domain,
      updateTime: domain.updateTime?.split('.')[0].replace('T', ' '),
      createTime: domain.createTime?.split('.')[0].replace('T', ' ')
    }
  })

  return domains
})

// 初始化
onMounted(async () => {
  await domainStore.fetchDomains()
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

// 导航到领域设置页面
const navigateToDomainSetting = (domain?: any) => {
  if (domain) {
    // 编辑领域
    domainStore.setCurrentDomain(domain)
    router.push(`/meta/domain/setting?mode=edit&domainId=${domain.domainId}`)
  } else {
    // 创建领域
    router.push('/meta/domain/setting?mode=create')
  }
}

// 进入领域平台
const navigateToDomainPlatform = (row: any) => {
  domainStore.setCurrentDomain(row)
  if(row.status !== '1'){
    ElMessage.warning('请先发布')
  }else if(row.url === ''){
    ElMessage.warning('领域地址不能为空')
  }else{
    window.open(row.url)
  }
}

// 查看场景
const handleViewScenes = (row: any) => {
  domainStore.setCurrentDomain(row)
  window.open(`/#/domain/scene/list?domainId=${row.domainId}`)
}

// 删除领域
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除领域 "${row.domainName}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(async () => {
    try {
      await domainStore.deleteDomain(row.domainId)
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
.domain-list-container {
  padding: 20px;
}

.domain-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.domain-search {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}
</style>